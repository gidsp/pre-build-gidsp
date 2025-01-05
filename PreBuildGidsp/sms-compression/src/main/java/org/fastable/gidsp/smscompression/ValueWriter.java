package org.fastable.gidsp.smscompression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.fastable.gidsp.smscompression.SmsConsts.MetadataType;
import org.fastable.gidsp.smscompression.models.SmsAttributeValue;
import org.fastable.gidsp.smscompression.models.SmsDataValue;
import org.fastable.gidsp.smscompression.models.SmsMetadata;
import org.fastable.gidsp.smscompression.models.SmsValue;
import org.fastable.gidsp.smscompression.models.Uid;
import org.fastable.gidsp.smscompression.utils.BinaryUtils;
import org.fastable.gidsp.smscompression.utils.BitOutputStream;
import org.fastable.gidsp.smscompression.utils.IdUtil;
import org.fastable.gidsp.smscompression.utils.ValueUtil;

public class ValueWriter
{
    BitOutputStream outStream;

    SmsMetadata meta;

    boolean hashingEnabled;

    public ValueWriter( BitOutputStream outStream, SmsMetadata meta, boolean hashingEnabled )
    {
        this.outStream = outStream;
        this.meta = meta;
        this.hashingEnabled = hashingEnabled;
    }

    public int writeHashBitLen( MetadataType type, boolean useHashing )
        throws SmsCompressionException
    {
        if ( !useHashing )
            return 0;

        int hashBitLen = IdUtil.getBitLengthForList( meta.getType( type ) );
        outStream.write( hashBitLen, SmsConsts.VARLEN_BITLEN );
        return hashBitLen;
    }

    public void writeValID( Uid uid, int bitLen, boolean useHashing )
        throws SmsCompressionException
    {
        if ( !useHashing )
        {
            ValueUtil.writeString( uid.getUid(), outStream );
            return;
        }

        // We have a non-empty list of UIDs in our metadata with hashing
        // enabled, we expect to find all UIDs in the metadata for this type
        if ( !meta.getType( uid.getType() ).contains( uid.getUid() ) )
            throw new SmsCompressionException(
                String.format( "Error hashing UID [%s] not found in [%s]", uid.getUid(), uid.getType() ) );

        int idHash = BinaryUtils.hash( uid.getUid(), bitLen );
        outStream.write( idHash, bitLen );
    }

    public void writeAttributeValues( List<SmsAttributeValue> values )
        throws SmsCompressionException
    {
        MetadataType attrType = MetadataType.TRACKED_ENTITY_ATTRIBUTE;
        boolean useHashing = hashingEnabled && meta != null && meta.getType( attrType ) != null
            && !meta.getType( attrType ).isEmpty();
        ValueUtil.writeBool( useHashing, outStream );
        int attributeBitLen = writeHashBitLen( attrType, useHashing );

        List<SmsValue<?>> smsVals = new ArrayList<>();
        for ( SmsAttributeValue val : values )
        {
            smsVals.add( val.getSmsValue() );
        }
        int fixedIntBitLen = ValueUtil.getBitlenLargestInt( smsVals );
        // We shift the bitlen down one to allow the max
        outStream.write( fixedIntBitLen - 1, SmsConsts.FIXED_INT_BITLEN );

        for ( Iterator<SmsAttributeValue> valIter = values.iterator(); valIter.hasNext(); )
        {
            SmsAttributeValue val = valIter.next();
            writeValID( val.getAttribute(), attributeBitLen, useHashing );
            ValueUtil.writeSmsValue( val.getSmsValue(), fixedIntBitLen, outStream );

            int separator = valIter.hasNext() ? 1 : 0;
            outStream.write( separator, 1 );
        }
    }

    public Map<Uid, List<SmsDataValue>> groupDataValues( List<SmsDataValue> values )
    {
        HashMap<Uid, List<SmsDataValue>> map = new HashMap<>();
        for ( SmsDataValue val : values )
        {
            Uid catOptionCombo = val.getCategoryOptionCombo();
            if ( !map.containsKey( catOptionCombo ) )
            {
                ArrayList<SmsDataValue> list = new ArrayList<>();
                map.put( catOptionCombo, list );
            }
            List<SmsDataValue> list = map.get( catOptionCombo );
            list.add( val );
        }
        return map;
    }

    public void writeDataValues( List<SmsDataValue> values )
        throws SmsCompressionException
    {
        MetadataType cocType = MetadataType.CATEGORY_OPTION_COMBO;
        MetadataType deType = MetadataType.DATA_ELEMENT;
        boolean useHashing = hashingEnabled && meta != null && meta.getType( cocType ) != null
            && !meta.getType( cocType ).isEmpty() && meta.getType( deType ) != null
            && !meta.getType( deType ).isEmpty();

        ValueUtil.writeBool( useHashing, outStream );
        int catOptionComboBitLen = writeHashBitLen( cocType, useHashing );
        int dataElementBitLen = writeHashBitLen( deType, useHashing );

        List<SmsValue<?>> smsVals = new ArrayList<>();
        for ( SmsDataValue val : values )
        {
            smsVals.add( val.getSmsValue() );
        }
        int fixedIntBitLen = ValueUtil.getBitlenLargestInt( smsVals );
        // We shift the bitlen down one to allow the max
        outStream.write( fixedIntBitLen - 1, SmsConsts.FIXED_INT_BITLEN );

        Map<Uid, List<SmsDataValue>> valMap = groupDataValues( values );

        for ( Iterator<Uid> keyIter = valMap.keySet().iterator(); keyIter.hasNext(); )
        {
            Uid catOptionCombo = keyIter.next();
            writeValID( catOptionCombo, catOptionComboBitLen, useHashing );
            List<SmsDataValue> vals = valMap.get( catOptionCombo );

            for ( Iterator<SmsDataValue> valIter = vals.iterator(); valIter.hasNext(); )
            {
                SmsDataValue val = valIter.next();
                writeValID( val.getDataElement(), dataElementBitLen, useHashing );
                ValueUtil.writeSmsValue( val.getSmsValue(), fixedIntBitLen, outStream );

                int separator = valIter.hasNext() ? 1 : 0;
                outStream.write( separator, 1 );
            }

            int separator = keyIter.hasNext() ? 1 : 0;
            outStream.write( separator, 1 );
        }
    }
}

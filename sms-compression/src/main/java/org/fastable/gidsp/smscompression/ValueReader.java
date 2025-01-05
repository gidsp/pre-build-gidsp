package org.fastable.gidsp.smscompression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fastable.gidsp.smscompression.SmsConsts.MetadataType;
import org.fastable.gidsp.smscompression.models.SmsAttributeValue;
import org.fastable.gidsp.smscompression.models.SmsDataValue;
import org.fastable.gidsp.smscompression.models.SmsMetadata;
import org.fastable.gidsp.smscompression.models.SmsValue;
import org.fastable.gidsp.smscompression.models.Uid;
import org.fastable.gidsp.smscompression.utils.BitInputStream;
import org.fastable.gidsp.smscompression.utils.IdUtil;
import org.fastable.gidsp.smscompression.utils.ValueUtil;

public class ValueReader
{
    BitInputStream inStream;

    SmsMetadata meta;

    boolean hashingEnabled;

    public ValueReader( BitInputStream inStream, SmsMetadata meta )
    {
        this.inStream = inStream;
        this.meta = meta;
    }

    public Uid readValId( int bitLen, Map<Integer, String> idLookup, MetadataType type )
        throws SmsCompressionException
    {
        if ( !hashingEnabled )
            return new Uid( ValueUtil.readString( inStream ), type );

        String id;
        int idHash = inStream.read( bitLen );
        id = idLookup.get( idHash );
        return new Uid( id, idHash, type );
    }

    public List<SmsAttributeValue> readAttributeValues()
        throws SmsCompressionException
    {
        ArrayList<SmsAttributeValue> values = new ArrayList<>();
        MetadataType type = MetadataType.TRACKED_ENTITY_ATTRIBUTE;
        int attributeBitLen = 0;
        Map<Integer, String> idLookup = null;

        this.hashingEnabled = ValueUtil.readBool( inStream );
        if ( hashingEnabled )
        {
            attributeBitLen = inStream.read( SmsConsts.VARLEN_BITLEN );
            idLookup = IdUtil.getIdLookup( meta.getType( type ), attributeBitLen );
        }
        int fixedIntBitLen = inStream.read( SmsConsts.FIXED_INT_BITLEN ) + 1;

        for ( int valSep = 1; valSep == 1; valSep = inStream.read( 1 ) )
        {
            Uid id = readValId( attributeBitLen, idLookup, type );
            SmsValue<?> smsValue = ValueUtil.readSmsValue( fixedIntBitLen, inStream );
            values.add( new SmsAttributeValue( id, smsValue ) );
        }

        return values;
    }

    public List<SmsDataValue> readDataValues()
        throws SmsCompressionException
    {
        int catOptionComboBitLen = 0;
        int dataElementBitLen = 0;
        Map<Integer, String> cocIDLookup = null;
        Map<Integer, String> deIDLookup = null;
        MetadataType cocType = MetadataType.CATEGORY_OPTION_COMBO;
        MetadataType deType = MetadataType.DATA_ELEMENT;

        this.hashingEnabled = ValueUtil.readBool( inStream );
        if ( hashingEnabled )
        {
            catOptionComboBitLen = inStream.read( SmsConsts.VARLEN_BITLEN );
            cocIDLookup = IdUtil.getIdLookup( meta.getType( cocType ), catOptionComboBitLen );

            dataElementBitLen = inStream.read( SmsConsts.VARLEN_BITLEN );
            deIDLookup = IdUtil.getIdLookup( meta.getType( deType ), dataElementBitLen );
        }
        int fixedIntBitLen = inStream.read( SmsConsts.FIXED_INT_BITLEN ) + 1;
        ArrayList<SmsDataValue> values = new ArrayList<>();

        for ( int cocSep = 1; cocSep == 1; cocSep = inStream.read( 1 ) )
        {
            Uid catOptionCombo = readValId( catOptionComboBitLen, cocIDLookup, cocType );

            for ( int valSep = 1; valSep == 1; valSep = inStream.read( 1 ) )
            {
                Uid dataElement = readValId( dataElementBitLen, deIDLookup, deType );
                SmsValue<?> smsValue = ValueUtil.readSmsValue( fixedIntBitLen, inStream );
                values.add( new SmsDataValue( catOptionCombo, dataElement, smsValue ) );
            }
        }

        return values;
    }
}

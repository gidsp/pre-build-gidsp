package org.fastable.gidsp.smscompression.utils;

import java.util.Date;
import java.util.List;

import org.fastable.gidsp.smscompression.SmsCompressionException;
import org.fastable.gidsp.smscompression.SmsConsts;
import org.fastable.gidsp.smscompression.SmsConsts.ValueType;
import org.fastable.gidsp.smscompression.models.SmsValue;

public class ValueUtil
{
    // TODO: Find a better way to pass down fixedIntBitLen
    public static void writeSmsValue( SmsValue<?> val, int fixedIntBitlen, BitOutputStream outStream )
        throws SmsCompressionException
    {
        // First write out the value type
        outStream.write( val.getType().ordinal(), SmsConsts.VALTYPE_BITLEN );

        // Now write out the actual value
        switch ( val.getType() )
        {
        case BOOL:
            writeBool( (Boolean) val.getValue(), outStream );
            break;
        case DATE:
            writeDate( (Date) val.getValue(), outStream );
            break;
        case INT:
            writeInt( (Integer) val.getValue(), fixedIntBitlen, outStream );
            break;
        case FLOAT:
            writeFloat( (Float) val.getValue(), outStream );
            break;
        // STRING is the default case
        default:
            writeString( (String) val.getValue(), outStream );
        }
    }

    // TODO: Find a better way to pass down fixedIntBitLen
    public static SmsValue<?> readSmsValue( int fixedIntBitLen, BitInputStream inStream )
        throws SmsCompressionException
    {
        // First read the value type
        int typeNum = inStream.read( SmsConsts.VALTYPE_BITLEN );
        ValueType type = ValueType.values()[typeNum];

        // Now read the actual value
        switch ( type )
        {
        case BOOL:
            return new SmsValue<Boolean>( readBool( inStream ), type );
        case DATE:
            return new SmsValue<Date>( readDate( inStream ), type );
        case INT:
            return new SmsValue<Integer>( readInt( fixedIntBitLen, inStream ), type );
        case FLOAT:
            return new SmsValue<Float>( readFloat( inStream ), type );
        case STRING:
            return new SmsValue<String>( readString( inStream ), type );
        default:
            throw new SmsCompressionException( "Unknown ValueType: " + type );
        }
    }

    public static void writeBool( boolean val, BitOutputStream outStream )
        throws SmsCompressionException
    {
        int intVal = val ? 1 : 0;
        outStream.write( intVal, 1 );
    }

    public static boolean readBool( BitInputStream inStream )
        throws SmsCompressionException
    {
        int intVal = inStream.read( 1 );
        return intVal == 1;
    }

    public static void writeDate( Date d, BitOutputStream outStream )
        throws SmsCompressionException
    {
        long epochSecs = d.getTime() / 1000;
        outStream.write( (int) epochSecs, SmsConsts.EPOCH_DATE_BITLEN );
    }

    public static Date readDate( BitInputStream inStream )
        throws SmsCompressionException
    {
        long epochSecs = inStream.read( SmsConsts.EPOCH_DATE_BITLEN );
        Date dateVal = new Date( epochSecs * 1000 );
        return dateVal;
    }

    public static void writeInt( int val, int fixedIntBitlen, BitOutputStream outStream )
        throws SmsCompressionException
    {
        // We can't write negative ints so we handle sign separately
        int isNegative = val < 0 ? 1 : 0;
        outStream.write( isNegative, 1 );
        val = Math.abs( val );

        // If it's bigger than the fixedInt size we need to give its size
        int isVariable = val > SmsConsts.MAX_FIXED_NUM ? 1 : 0;
        outStream.write( isVariable, 1 );
        int intBitlen = BinaryUtils.bitlenNeeded( val );

        if ( isVariable == 1 )
            outStream.write( intBitlen, SmsConsts.VARLEN_BITLEN );

        int bitLen = isVariable == 1 ? intBitlen : fixedIntBitlen;

        outStream.write( val, bitLen );
    }

    public static int readInt( int fixedIntBitlen, BitInputStream inStream )
        throws SmsCompressionException
    {
        // Is this int negative?
        int setNegative = inStream.read( 1 ) == 1 ? -1 : 1;

        // Is this int fixed or variable size?
        int isVariable = inStream.read( 1 );

        int bitLen = isVariable == 1 ? inStream.read( SmsConsts.VARLEN_BITLEN ) : fixedIntBitlen;
        return setNegative * inStream.read( bitLen );
    }

    public static void writeFloat( float val, BitOutputStream outStream )
        throws SmsCompressionException
    {
        // TODO: We need to handle floats better
        writeString( Float.toString( val ), outStream );
    }

    public static float readFloat( BitInputStream inStream )
        throws SmsCompressionException
    {
        // TODO: We need to handle floats better
        return Float.parseFloat( readString( inStream ) );
    }

    public static void writeString( String s, BitOutputStream outStream )
        throws SmsCompressionException
    {
        for ( char c : s.toCharArray() )
        {
            outStream.write( c, SmsConsts.CHAR_BITLEN );
        }
        // Null terminator
        outStream.write( 0, SmsConsts.CHAR_BITLEN );
    }

    public static String readString( BitInputStream inStream )
        throws SmsCompressionException
    {
        String s = "";
        do
        {
            int i = inStream.read( SmsConsts.CHAR_BITLEN );
            if ( i == 0 )
                break;
            s += (char) i;
        }
        while ( true );
        return s;
    }

    public static int getBitlenLargestInt( List<SmsValue<?>> values )
    {
        int maxInt = 0;
        for ( SmsValue<?> val : values )
        {
            if ( val.getType() == ValueType.INT )
            {
                int intVal = Math.abs( (Integer) val.getValue() );
                if ( intVal > SmsConsts.MAX_FIXED_NUM )
                    continue;
                maxInt = intVal > maxInt ? intVal : maxInt;
            }
        }
        return BinaryUtils.bitlenNeeded( maxInt );
    }
}

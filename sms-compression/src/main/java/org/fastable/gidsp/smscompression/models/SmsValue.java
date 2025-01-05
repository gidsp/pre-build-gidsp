package org.fastable.gidsp.smscompression.models;

import java.text.ParseException;
import java.util.Date;

import org.fastable.gidsp.smscompression.SmsConsts;
import org.fastable.gidsp.smscompression.SmsConsts.ValueType;

public class SmsValue<T>
{
    T value;

    ValueType type;

    public SmsValue( T value, ValueType type )
    {
        this.value = value;
        this.type = type;
    }

    public T getValue()
    {
        return this.value;

    }

    public ValueType getType()
    {
        return this.type;
    }

    public static SmsValue<?> asSmsValue( String value )
    {
        // BOOL
        if ( value.equals( "true" ) || value.equals( "false" ) )
        {
            Boolean valBool = value.equals( "true" ) ? true : false;
            return new SmsValue<Boolean>( valBool, ValueType.BOOL );
        }

        // DATE
        try
        {
            Date valDate = SmsConsts.SIMPLE_DATE_FORMAT.parse( value );
            return new SmsValue<Date>( valDate, ValueType.DATE );
        }
        catch ( ParseException e )
        {
            // not a date
        }

        // INT
        try
        {
            Integer valInt = Integer.parseInt( value );
            return new SmsValue<Integer>( valInt, ValueType.INT );
        }
        catch ( NumberFormatException e )
        {
            // not an integer
        }

        // FLOAT
        try
        {
            Float valFloat = Float.parseFloat( value );
            return new SmsValue<Float>( valFloat, ValueType.FLOAT );
        }
        catch ( NumberFormatException e )
        {
            // not a float
        }

        // If all else fails we can use String
        return new SmsValue<String>( value, ValueType.STRING );
    }
}

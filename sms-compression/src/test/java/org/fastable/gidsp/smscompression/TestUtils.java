package org.fastable.gidsp.smscompression;

/*
 * Copyright (c) 2004-2019, General Intergrate Date Service Platform
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.fastable.gidsp.smscompression.SmsConsts.SmsEventStatus;
import org.fastable.gidsp.smscompression.models.GeoPoint;
import org.fastable.gidsp.smscompression.models.SmsDataValue;
import org.fastable.gidsp.smscompression.models.SmsEvent;
import org.fastable.gidsp.smscompression.models.SmsSubmission;
import org.junit.Assert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestUtils
{

    public static Date getNowWithoutMillis()
    {
        Calendar cal = Calendar.getInstance();
        cal.set( Calendar.MILLISECOND, 0 );
        return cal.getTime();
    }

    public static List<SmsEvent> createEventList()
    {
        List<SmsEvent> events = new ArrayList<>();
        for ( int i = 1; i <= 3; i++ )
        {
            SmsEvent event = new SmsEvent();
            event.setOrgUnit( "DiszpKrYNg8" ); // Ngelehun CHC
            event.setProgramStage( "A03MvHHogjR" ); // Birth
            event.setEventStatus( SmsEventStatus.COMPLETED );
            event.setAttributeOptionCombo( "HllvX50cXC0" ); // Default
                                                            // catOptionCombo
            event.setEvent( "r7M1gUFK3X" + i ); // New UID
            event.setEventDate( getNowWithoutMillis() );
            event.setDueDate( getNowWithoutMillis() );
            event.setCoordinates( new GeoPoint( 8.4844694f, -13.2364332f ) );
            ArrayList<SmsDataValue> values = new ArrayList<>();
            values.add( new SmsDataValue( "HllvX50cXC0", "UXz7xuGCEhU", String.valueOf( i + 1 ) ) ); // Weight
            event.setValues( values );
            events.add( event );
        }

        return events;
    }

    public static void printSubm( SmsSubmission subm )
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println( gson.toJson( subm ) );
    }

    public static String encBase64( byte[] subm )
    {
        return Base64.getEncoder().encodeToString( subm );
    }

    public static byte[] decBase64( String subm )
    {
        return Base64.getDecoder().decode( subm );
    }

    public static String stripTillValid( String subm )
    {
        while ( !subm.isEmpty() )
        {
            try
            {
                Base64.getDecoder().decode( subm );
                return subm;
            }
            catch ( Exception e )
            {
                subm = subm.subSequence( 0, subm.length() - 1 ).toString();
            }
        }
        return subm;
    }

    public static void printBase64Subm( String subm, Class<?> submType )
    {
        System.out.println( submType );
        System.out.println( "Base64 encoding is: " + subm );
        System.out.println( "Char length: " + subm.length() );
        System.out.println( "Num SMS: " + ((subm.length() / 160) + 1) );
        System.out.println( "************************" );
    }

    public static void checkSubmissionsAreEqual( SmsSubmission origSubm, SmsSubmission decSubm )
    {
        if ( !origSubm.equals( decSubm ) )
        {
            System.out.println( "Submissions are not equal!" );
            System.out.println( "Original submission: " );
            printSubm( origSubm );
            System.out.println( "Decoded submission: " );
            printSubm( decSubm );
            Assert.fail();
        }
    }
}

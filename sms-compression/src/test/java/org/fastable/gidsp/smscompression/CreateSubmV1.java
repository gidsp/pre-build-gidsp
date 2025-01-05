package org.fastable.gidsp.smscompression;

import java.util.ArrayList;

import org.fastable.gidsp.smscompression.SmsConsts.SmsEventStatus;
import org.fastable.gidsp.smscompression.models.EnrollmentSmsSubmission;
import org.fastable.gidsp.smscompression.models.SmsAttributeValue;
import org.fastable.gidsp.smscompression.models.SmsDataValue;
import org.fastable.gidsp.smscompression.models.SimpleEventSmsSubmission;
import org.fastable.gidsp.smscompression.models.TrackerEventSmsSubmission;

public class CreateSubmV1
{
    public static EnrollmentSmsSubmission createEnrollmentSubmissionV1()
    {
        EnrollmentSmsSubmission subm = new EnrollmentSmsSubmission();

        subm.setUserId( "GOLswS44mh8" ); // Tom Wakiki (system)
        subm.setOrgUnit( "DiszpKrYNg8" ); // Ngelehun CHC
        subm.setTrackerProgram( "IpHINAT79UW" ); // Child Programme
        subm.setTrackedEntityType( "nEenWmSyUEp" ); // Person
        subm.setTrackedEntityInstance( "T2bRuLEGoVN" ); // Newly generated UID
        subm.setEnrollment( "p7M1gUFK37W" ); // Newly generated UID
        subm.setEnrollmentDate( TestUtils.getNowWithoutMillis() );
        ArrayList<SmsAttributeValue> values = new ArrayList<>();
        values.add( new SmsAttributeValue( "w75KJ2mc4zz", "Harold" ) ); // First
                                                                        // Name
        values.add( new SmsAttributeValue( "zDhUuAYrxNC", "Smith" ) ); // Last
                                                                       // Name
        values.add( new SmsAttributeValue( "FO4sWYJ64LQ", "Sydney" ) ); // City
        values.add( new SmsAttributeValue( "VqEFza8wbwA", "The Opera House" ) ); // Address
        values.add( new SmsAttributeValue( "lZGmxYbs97q", "987123" ) ); // Unique
                                                                        // ID
        subm.setValues( values );
        subm.setSubmissionId( 1 );

        return subm;
    }

    public static SimpleEventSmsSubmission createSimpleEventSubmissionV1()
    {
        SimpleEventSmsSubmission subm = new SimpleEventSmsSubmission();

        subm.setUserId( "GOLswS44mh8" ); // Tom Wakiki (system)
        subm.setOrgUnit( "DiszpKrYNg8" ); // Ngelehun CHC
        subm.setEventProgram( "lxAQ7Zs9VYR" ); // Antenatal Care Visit
        subm.setEventStatus( SmsEventStatus.COMPLETED );
        subm.setAttributeOptionCombo( "HllvX50cXC0" ); // Default catOptionCombo
        subm.setEvent( "l7M1gUFK37v" ); // New UID
        subm.setEventDate( TestUtils.getNowWithoutMillis() );
        ArrayList<SmsDataValue> values = new ArrayList<>();
        values.add( new SmsDataValue( "HllvX50cXC0", "sWoqcoByYmD", "true" ) ); // WHOMCH
                                                                                // Smoking
        values.add( new SmsDataValue( "HllvX50cXC0", "Ok9OQpitjQr", "false" ) ); // WHOMCH
                                                                                 // Smoking
                                                                                 // cessation
                                                                                 // counselling
                                                                                 // provided
        values.add( new SmsDataValue( "HllvX50cXC0", "vANAXwtLwcT", "14" ) ); // WHOMCH
                                                                              // Hemoglobin
                                                                              // value
        subm.setValues( values );
        subm.setSubmissionId( 1 );

        return subm;
    }

    public static TrackerEventSmsSubmission createTrackerEventSubmissionV1()
    {
        TrackerEventSmsSubmission subm = new TrackerEventSmsSubmission();

        subm.setUserId( "GOLswS44mh8" ); // Tom Wakiki (system)
        subm.setOrgUnit( "DiszpKrYNg8" ); // Ngelehun CHC
        subm.setProgramStage( "A03MvHHogjR" ); // Birth
        subm.setAttributeOptionCombo( "HllvX50cXC0" ); // Default catOptionCombo
        subm.setEnrollment( "DacGG5vK1K6" ); // Test Person
        subm.setEvent( "r7M1gUFK37v" ); // New UID
        subm.setEventStatus( SmsEventStatus.COMPLETED );
        subm.setEventDate( TestUtils.getNowWithoutMillis() );
        ArrayList<SmsDataValue> values = new ArrayList<>();
        values.add( new SmsDataValue( "HllvX50cXC0", "a3kGcGDCuk6", "10" ) ); // Apgar
                                                                              // score

        values.add( new SmsDataValue( "HllvX50cXC0", "wQLfBvPrXqq", "Others" ) ); // ARV
                                                                                  // at
                                                                                  // birth
        values.add( new SmsDataValue( "HllvX50cXC0", "X8zyunlgUfM", "Exclusive" ) ); // Infant
                                                                                     // feeding
        subm.setValues( values );
        subm.setSubmissionId( 1 );

        return subm;
    }
}

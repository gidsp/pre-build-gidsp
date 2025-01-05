package org.fastable.gidsp.smscompression;

import java.util.ArrayList;

import org.fastable.gidsp.smscompression.SmsConsts.SmsEnrollmentStatus;
import org.fastable.gidsp.smscompression.SmsConsts.SmsEventStatus;
import org.fastable.gidsp.smscompression.models.AggregateDatasetSmsSubmission;
import org.fastable.gidsp.smscompression.models.DeleteSmsSubmission;
import org.fastable.gidsp.smscompression.models.EnrollmentSmsSubmission;
import org.fastable.gidsp.smscompression.models.GeoPoint;
import org.fastable.gidsp.smscompression.models.RelationshipSmsSubmission;
import org.fastable.gidsp.smscompression.models.SmsAttributeValue;
import org.fastable.gidsp.smscompression.models.SmsDataValue;
import org.fastable.gidsp.smscompression.models.SimpleEventSmsSubmission;
import org.fastable.gidsp.smscompression.models.TrackerEventSmsSubmission;

public class CreateSubm
{
    public static DeleteSmsSubmission createDeleteSubmission()
    {
        DeleteSmsSubmission subm = new DeleteSmsSubmission();

        subm.setUserId( "GOLswS44mh8" ); // Tom Wakiki (system)
        subm.setEvent( "Jpr20TLJ7Z1" ); // Generated UID of test event
        subm.setSubmissionId( 1 );

        return subm;
    }

    public static RelationshipSmsSubmission createRelationshipSubmission()
    {
        RelationshipSmsSubmission subm = new RelationshipSmsSubmission();

        subm.setUserId( "GOLswS44mh8" ); // Tom Wakiki (system)
        subm.setRelationshipType( "XdP5nraLPZ0" ); // Sibling_a-to-b_(Person-Person)
        subm.setRelationship( "uf3svrmpzOj" ); // Generated UID for new
                                               // relationship
        subm.setFrom( "qv0j4JBXQX0" ); // Gloria Murray (Person)
        subm.setTo( "LSEjy8nA3kY" ); // Jerald Wilson (Person)
        subm.setSubmissionId( 1 );

        return subm;
    }

    public static SimpleEventSmsSubmission createSimpleEventSubmission()
    {
        SimpleEventSmsSubmission subm = new SimpleEventSmsSubmission();

        subm.setUserId( "GOLswS44mh8" ); // Tom Wakiki (system)
        subm.setOrgUnit( "DiszpKrYNg8" ); // Ngelehun CHC
        subm.setEventProgram( "lxAQ7Zs9VYR" ); // Antenatal Care Visit
        subm.setEventStatus( SmsEventStatus.COMPLETED );
        subm.setAttributeOptionCombo( "HllvX50cXC0" ); // Default catOptionCombo
        subm.setEvent( "l7M1gUFK37v" ); // New UID
        subm.setEventDate( TestUtils.getNowWithoutMillis() );
        subm.setDueDate( TestUtils.getNowWithoutMillis() );
        subm.setCoordinates( new GeoPoint( 8.4844694f, -13.2364332f ) );
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

    public static AggregateDatasetSmsSubmission createAggregateDatasetSubmission()
    {
        AggregateDatasetSmsSubmission subm = new AggregateDatasetSmsSubmission();

        subm.setUserId( "GOLswS44mh8" ); // Tom Wakiki (system)
        subm.setOrgUnit( "DiszpKrYNg8" ); // Ngelehun CHC
        subm.setDataSet( "Nyh6laLdBEJ" ); // IDSR Weekly
        subm.setComplete( true );
        subm.setAttributeOptionCombo( "HllvX50cXC0" );
        subm.setPeriod( "2019W16" );
        ArrayList<SmsDataValue> values = new ArrayList<>();
        values.add( new SmsDataValue( "HllvX50cXC0", "UsSUX0cpKsH", "0" ) ); // Cholera
        values.add( new SmsDataValue( "HllvX50cXC0", "HS9zqaBdOQ4", "-65535" ) ); // Plague
        values.add( new SmsDataValue( "HllvX50cXC0", "noIzB569hTM", "12345678" ) ); // Yellow
                                                                                    // Fever
        values.add( new SmsDataValue( "HllvX50cXC0", "vq2qO3eTrNi", "-24.5" ) ); // Malaria
        values.add( new SmsDataValue( "HllvX50cXC0", "YazgqXbizv1", "0.12345678" ) ); // Measles
        subm.setValues( values );
        subm.setSubmissionId( 1 );

        return subm;
    }

    public static EnrollmentSmsSubmission createEnrollmentSubmission()
    {
        EnrollmentSmsSubmission subm = new EnrollmentSmsSubmission();

        subm.setUserId( "GOLswS44mh8" ); // Tom Wakiki (system)
        subm.setOrgUnit( "DiszpKrYNg8" ); // Ngelehun CHC
        subm.setTrackerProgram( "IpHINAT79UW" ); // Child Programme
        subm.setTrackedEntityType( "nEenWmSyUEp" ); // Person
        subm.setTrackedEntityInstance( "T2bRuLEGoVN" ); // Newly generated UID
        subm.setEnrollment( "p7M1gUFK37W" ); // Newly generated UID
        subm.setEnrollmentDate( TestUtils.getNowWithoutMillis() );
        subm.setIncidentDate( TestUtils.getNowWithoutMillis() );
        subm.setCoordinates( new GeoPoint( 8.4844694f, -13.2364332f ) );
        subm.setEnrollmentStatus( SmsEnrollmentStatus.ACTIVE );
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

        subm.setEvents( TestUtils.createEventList() );

        return subm;
    }

    public static TrackerEventSmsSubmission createTrackerEventSubmission()
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
        subm.setDueDate( TestUtils.getNowWithoutMillis() );
        subm.setCoordinates( new GeoPoint( 8.4844694f, -13.2364332f ) );
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

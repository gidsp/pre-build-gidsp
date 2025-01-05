package org.fastable.gidsp.smscompression;

import java.io.FileReader;

import org.apache.commons.io.IOUtils;
import org.fastable.gidsp.smscompression.models.AggregateDatasetSmsSubmission;
import org.fastable.gidsp.smscompression.models.EnrollmentSmsSubmission;
import org.fastable.gidsp.smscompression.models.SmsMetadata;
import org.fastable.gidsp.smscompression.models.SmsSubmission;
import org.fastable.gidsp.smscompression.models.SmsSubmissionHeader;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

public class TestCustomMetadata
{

    SmsMetadata meta;

    SmsSubmissionWriter writer;

    SmsSubmissionReader reader;

    public String compressSubm( SmsSubmission subm )
        throws Exception
    {
        byte[] compressSubm = writer.compress( subm );
        String comp64 = TestUtils.encBase64( compressSubm );
        TestUtils.printBase64Subm( comp64, subm.getClass() );
        return comp64;
    }

    public SmsSubmission decompressSubm( String comp64 )
        throws Exception
    {
        byte[] decSubmBytes = TestUtils.decBase64( comp64 );
        SmsSubmissionHeader header = reader.readHeader( decSubmBytes );
        Assert.assertNotNull( header );
        return reader.readSubmission( decSubmBytes, meta );
    }

    @Test
    public void testNullMetadata()
    {
        try
        {
            meta = null;
            writer = new SmsSubmissionWriter( meta );
            reader = new SmsSubmissionReader();

            AggregateDatasetSmsSubmission origSubm = CreateSubm.createAggregateDatasetSubmission();
            String comp64 = compressSubm( origSubm );
            AggregateDatasetSmsSubmission decSubm = (AggregateDatasetSmsSubmission) decompressSubm( comp64 );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testEmptyMetadata()
    {
        try
        {
            meta = new SmsMetadata();
            writer = new SmsSubmissionWriter( meta );
            reader = new SmsSubmissionReader();

            AggregateDatasetSmsSubmission origSubm = CreateSubm.createAggregateDatasetSubmission();
            String comp64 = compressSubm( origSubm );
            AggregateDatasetSmsSubmission decSubm = (AggregateDatasetSmsSubmission) decompressSubm( comp64 );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testEmptyDataElementsMetadata()
    {
        try
        {
            Gson gson = new Gson();
            String metadataJson = IOUtils.toString( new FileReader( "src/test/resources/metadata.json" ) );
            meta = gson.fromJson( metadataJson, SmsMetadata.class );
            meta.dataElements = null;
            writer = new SmsSubmissionWriter( meta );
            reader = new SmsSubmissionReader();

            AggregateDatasetSmsSubmission origSubm = CreateSubm.createAggregateDatasetSubmission();
            String comp64 = compressSubm( origSubm );
            AggregateDatasetSmsSubmission decSubm = (AggregateDatasetSmsSubmission) decompressSubm( comp64 );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testEmptyAttributesMetadata()
    {
        try
        {
            Gson gson = new Gson();
            String metadataJson = IOUtils.toString( new FileReader( "src/test/resources/metadata.json" ) );
            meta = gson.fromJson( metadataJson, SmsMetadata.class );
            meta.trackedEntityAttributes = null;
            writer = new SmsSubmissionWriter( meta );
            reader = new SmsSubmissionReader();

            EnrollmentSmsSubmission origSubm = CreateSubm.createEnrollmentSubmission();
            String comp64 = compressSubm( origSubm );
            EnrollmentSmsSubmission decSubm = (EnrollmentSmsSubmission) decompressSubm( comp64 );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testHashingDisabled()
    {
        try
        {
            Gson gson = new Gson();
            String metadataJson = IOUtils.toString( new FileReader( "src/test/resources/metadata.json" ) );
            meta = gson.fromJson( metadataJson, SmsMetadata.class );
            writer = new SmsSubmissionWriter( meta );
            writer.setHashingEnabled( false );
            reader = new SmsSubmissionReader();

            AggregateDatasetSmsSubmission origSubm = CreateSubm.createAggregateDatasetSubmission();
            String comp64 = compressSubm( origSubm );
            AggregateDatasetSmsSubmission decSubm = (AggregateDatasetSmsSubmission) decompressSubm( comp64 );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

}

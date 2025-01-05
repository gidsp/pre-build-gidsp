package org.fastable.gidsp.smscompression;

import static org.junit.Assert.assertEquals;

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

import java.io.FileReader;

import org.apache.commons.io.IOUtils;
import org.fastable.gidsp.smscompression.models.AggregateDatasetSmsSubmission;
import org.fastable.gidsp.smscompression.models.DeleteSmsSubmission;
import org.fastable.gidsp.smscompression.models.EnrollmentSmsSubmission;
import org.fastable.gidsp.smscompression.models.RelationshipSmsSubmission;
import org.fastable.gidsp.smscompression.models.SimpleEventSmsSubmission;
import org.fastable.gidsp.smscompression.models.SmsMetadata;
import org.fastable.gidsp.smscompression.models.SmsSubmission;
import org.fastable.gidsp.smscompression.models.SmsSubmissionHeader;
import org.fastable.gidsp.smscompression.models.TrackerEventSmsSubmission;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

public class TestVersions
{
    SmsMetadata meta;

    SmsSubmissionWriter writer;

    SmsSubmissionReader reader;

    public String compressSubm( SmsSubmission subm, int version )
        throws Exception
    {
        byte[] compressSubm = writer.compress( subm, version );
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

    @Before
    public void init()
        throws Exception
    {
        Gson gson = new Gson();
        String metadataJson = IOUtils.toString( new FileReader( "src/test/resources/metadata.json" ) );
        meta = gson.fromJson( metadataJson, SmsMetadata.class );
        writer = new SmsSubmissionWriter( meta );
        reader = new SmsSubmissionReader();
    }

    @After
    public void cleanup()
    {

    }

    @Test
    public void testEncDecSimpleEventVersion1()
    {
        try
        {
            SimpleEventSmsSubmission origSubm = CreateSubmV1.createSimpleEventSubmissionV1();
            String comp64 = compressSubm( origSubm, 1 );
            SimpleEventSmsSubmission decSubm = (SimpleEventSmsSubmission) decompressSubm( comp64 );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testEncDecAggregateDatasetVersion1()
    {
        try
        {
            AggregateDatasetSmsSubmission origSubm = CreateSubm.createAggregateDatasetSubmission();
            String comp64 = compressSubm( origSubm, 1 );
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
    public void testEncDecEnrollmentVersion1()
    {
        try
        {
            EnrollmentSmsSubmission origSubm = CreateSubmV1.createEnrollmentSubmissionV1();
            String comp64 = compressSubm( origSubm, 1 );
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
    public void testEncDecTrackerEventVersion1()
    {
        try
        {
            TrackerEventSmsSubmission origSubm = CreateSubmV1.createTrackerEventSubmissionV1();
            String comp64 = compressSubm( origSubm, 1 );
            TrackerEventSmsSubmission decSubm = (TrackerEventSmsSubmission) decompressSubm( comp64 );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testEncDecDeleteVersion1()
    {
        try
        {
            DeleteSmsSubmission origSubm = CreateSubm.createDeleteSubmission();
            String comp64 = compressSubm( origSubm, 1 );
            DeleteSmsSubmission decSubm = (DeleteSmsSubmission) decompressSubm( comp64 );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testEncDecRelationshipVersion1()
    {
        try
        {
            RelationshipSmsSubmission origSubm = CreateSubm.createRelationshipSubmission();
            String comp64 = compressSubm( origSubm, 1 );
            RelationshipSmsSubmission decSubm = (RelationshipSmsSubmission) decompressSubm( comp64 );

            TestUtils.checkSubmissionsAreEqual( origSubm, decSubm );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Assert.fail( e.getMessage() );
        }
    }

    @Test
    public void testWriteUnknownVersion()
    {
        try
        {
            compressSubm( CreateSubm.createTrackerEventSubmission(), 0 );
        }
        catch ( Exception e )
        {
            assertEquals( e.getClass(), SmsCompressionException.class );
            assertEquals( e.getMessage(), "Version 0 of TrackerEventSmsSubmission is not supported" );
            return;
        }

        Assert.fail( "Expected unknown version exception not found" );
    }

    @Test
    public void testWriteFutureVersion()
    {
        SmsSubmission subm = CreateSubm.createTrackerEventSubmission();
        int futureVer = subm.getCurrentVersion() + 1;

        try
        {
            compressSubm( subm, futureVer );
        }
        catch ( Exception e )
        {
            assertEquals( e.getClass(), SmsCompressionException.class );
            assertEquals( e.getMessage(),
                String.format( "Version %d of TrackerEventSmsSubmission is not supported", futureVer ) );
            return;
        }

        Assert.fail( "Expected unknown version exception not found" );
    }

    @Test
    public void testWriteUnknownVersionRelationship()
    {
        try
        {
            compressSubm( CreateSubm.createRelationshipSubmission(), 0 );
        }
        catch ( Exception e )
        {
            assertEquals( e.getClass(), SmsCompressionException.class );
            assertEquals( e.getMessage(), "Version 0 of RelationshipSmsSubmission is not supported" );
            return;
        }

        Assert.fail( "Expected unknown version exception not found" );
    }

    @Test
    public void testWriteFutureVersionRelationship()
    {
        SmsSubmission subm = CreateSubm.createRelationshipSubmission();
        int futureVer = subm.getCurrentVersion() + 1;

        try
        {
            compressSubm( subm, futureVer );
        }
        catch ( Exception e )
        {
            assertEquals( e.getClass(), SmsCompressionException.class );
            assertEquals( e.getMessage(),
                String.format( "Version %d of RelationshipSmsSubmission is not supported", futureVer ) );
            return;
        }

        Assert.fail( "Expected unknown version exception not found" );
    }
}

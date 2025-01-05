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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.fastable.gidsp.smscompression.SmsConsts.MetadataType;
import org.fastable.gidsp.smscompression.SmsConsts.SmsEnrollmentStatus;
import org.fastable.gidsp.smscompression.SmsConsts.SmsEventStatus;
import org.fastable.gidsp.smscompression.SmsConsts.SubmissionType;
import org.fastable.gidsp.smscompression.models.AggregateDatasetSmsSubmission;
import org.fastable.gidsp.smscompression.models.DeleteSmsSubmission;
import org.fastable.gidsp.smscompression.models.EnrollmentSmsSubmission;
import org.fastable.gidsp.smscompression.models.GeoPoint;
import org.fastable.gidsp.smscompression.models.RelationshipSmsSubmission;
import org.fastable.gidsp.smscompression.models.SmsAttributeValue;
import org.fastable.gidsp.smscompression.models.SmsDataValue;
import org.fastable.gidsp.smscompression.models.SmsEvent;
import org.fastable.gidsp.smscompression.models.SmsMetadata;
import org.fastable.gidsp.smscompression.models.SmsSubmission;
import org.fastable.gidsp.smscompression.models.SmsSubmissionHeader;
import org.fastable.gidsp.smscompression.models.SimpleEventSmsSubmission;
import org.fastable.gidsp.smscompression.models.TrackerEventSmsSubmission;
import org.fastable.gidsp.smscompression.models.Uid;
import org.fastable.gidsp.smscompression.utils.BitInputStream;
import org.fastable.gidsp.smscompression.utils.IdUtil;
import org.fastable.gidsp.smscompression.utils.ValueUtil;

public class SmsSubmissionReader
{
    BitInputStream inStream;

    SmsMetadata meta;

    ValueReader valueReader;

    public SmsSubmissionHeader readHeader( byte[] smsBytes )
        throws SmsCompressionException
    {
        if ( !checkCrc( smsBytes ) )
            throw new SmsCompressionException( "Invalid CRC - CRC in header does not match submission" );

        ByteArrayInputStream byteStream = new ByteArrayInputStream( smsBytes );
        this.inStream = new BitInputStream( byteStream );
        inStream.read( SmsConsts.CRC_BITLEN ); // skip CRC

        SmsSubmissionHeader header = new SmsSubmissionHeader();
        header.readHeader( this );

        return header;
    }

    public SmsSubmission readSubmission( byte[] smsBytes, SmsMetadata meta )
        throws SmsCompressionException
    {
        if ( meta != null )
            meta.validate();
        this.meta = meta;
        SmsSubmissionHeader header = readHeader( smsBytes );
        this.valueReader = new ValueReader( inStream, meta );
        SmsSubmission subm = null;

        switch ( header.getType() )
        {
        case AGGREGATE_DATASET:
            subm = new AggregateDatasetSmsSubmission();
            break;
        case DELETE:
            subm = new DeleteSmsSubmission();
            break;
        case ENROLLMENT:
            subm = new EnrollmentSmsSubmission();
            break;
        case RELATIONSHIP:
            subm = new RelationshipSmsSubmission();
            break;
        case SIMPLE_EVENT:
            subm = new SimpleEventSmsSubmission();
            break;
        case TRACKER_EVENT:
            subm = new TrackerEventSmsSubmission();
            break;
        default:
            throw new SmsCompressionException( "Unknown SMS Submission Type: " + header.getType() );
        }

        subm.read( this, header );
        try
        {
            inStream.close();
        }
        catch ( IOException e )
        {
            throw new SmsCompressionException( e );
        }
        return subm;
    }

    private boolean checkCrc( byte[] smsBytes )
    {
        byte crc = smsBytes[0];
        byte[] submBytes = Arrays.copyOfRange( smsBytes, 1, smsBytes.length );

        try
        {
            MessageDigest digest = MessageDigest.getInstance( "SHA-256" );
            byte[] calcCrc = digest.digest( submBytes );
            return (calcCrc[0] == crc);
        }
        catch ( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
            return false;
        }
    }

    public SubmissionType readType()
        throws SmsCompressionException
    {
        int submType = inStream.read( SmsConsts.SUBM_TYPE_BITLEN );
        return SmsConsts.SubmissionType.values()[submType];
    }

    public int readVersion()
        throws SmsCompressionException
    {
        return inStream.read( SmsConsts.VERSION_BITLEN );
    }

    public Date readNonNullableDate()
        throws SmsCompressionException
    {
        return ValueUtil.readDate( inStream );
    }

    public Date readDate()
        throws SmsCompressionException
    {
        boolean hasDate = readBool();
        if ( !hasDate )
        {
            return null;

        }
        return ValueUtil.readDate( inStream );
    }

    public Uid readId( MetadataType type )
        throws SmsCompressionException
    {
        return IdUtil.readId( type, meta, inStream );
    }

    public String readNewId()
        throws SmsCompressionException
    {
        return IdUtil.readNewId( inStream );
    }

    public List<SmsAttributeValue> readAttributeValues()
        throws SmsCompressionException
    {
        return valueReader.readAttributeValues();
    }

    public List<SmsDataValue> readDataValues()
        throws SmsCompressionException
    {
        return valueReader.readDataValues();
    }

    public boolean readBool()
        throws SmsCompressionException
    {
        return ValueUtil.readBool( inStream );
    }

    // TODO: Update this once we have a better impl of period
    public String readPeriod()
        throws SmsCompressionException
    {
        return ValueUtil.readString( inStream );
    }

    public int readSubmissionId()
        throws SmsCompressionException
    {
        return inStream.read( SmsConsts.SUBM_ID_BITLEN );
    }

    public SmsEventStatus readEventStatus()
        throws SmsCompressionException
    {
        int eventStatusNum = inStream.read( SmsConsts.EVENT_STATUS_BITLEN );
        return SmsEventStatus.values()[eventStatusNum];
    }

    public List<SmsEvent> readEvents( int version )
        throws SmsCompressionException
    {
        boolean hasEvents = readBool();
        ArrayList<SmsEvent> events = null;
        if ( hasEvents )
        {
            events = new ArrayList<>();
            for ( boolean hasNext = true; hasNext; hasNext = readBool() )
            {
                SmsEvent event = new SmsEvent();
                event.readEvent( this, version );
                events.add( event );
            }
        }

        return events;
    }

    public GeoPoint readGeoPoint()
        throws SmsCompressionException
    {
        GeoPoint gp = null;
        boolean hasGeoPoint = readBool();
        if ( hasGeoPoint )
        {
            float lat = ValueUtil.readFloat( inStream );
            float lon = ValueUtil.readFloat( inStream );
            gp = new GeoPoint( lat, lon );
        }

        return gp;
    }

    public SmsEnrollmentStatus readEnrollmentStatus()
        throws SmsCompressionException
    {
        int enrollStatusNum = inStream.read( SmsConsts.ENROL_STATUS_BITLEN );
        return SmsEnrollmentStatus.values()[enrollStatusNum];
    }
}

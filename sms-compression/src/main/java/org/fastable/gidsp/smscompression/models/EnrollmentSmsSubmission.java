package org.fastable.gidsp.smscompression.models;

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

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.fastable.gidsp.smscompression.SmsCompressionException;
import org.fastable.gidsp.smscompression.SmsConsts;
import org.fastable.gidsp.smscompression.SmsConsts.MetadataType;
import org.fastable.gidsp.smscompression.SmsConsts.SmsEnrollmentStatus;
import org.fastable.gidsp.smscompression.SmsConsts.SubmissionType;
import org.fastable.gidsp.smscompression.SmsSubmissionReader;
import org.fastable.gidsp.smscompression.SmsSubmissionWriter;

public class EnrollmentSmsSubmission
    extends
    SmsSubmission
{
    protected Uid orgUnit;

    protected Uid trackerProgram;

    protected Uid trackedEntityType;

    protected Uid trackedEntityInstance;

    protected Uid enrollment;

    protected Date enrollmentDate;

    protected SmsEnrollmentStatus enrollmentStatus;

    protected Date incidentDate;

    protected GeoPoint coordinates;

    protected List<SmsAttributeValue> values;

    protected List<SmsEvent> events;

    public Uid getOrgUnit()
    {
        return orgUnit;
    }

    public void setOrgUnit( String orgUnit )
    {
        this.orgUnit = new Uid( orgUnit, MetadataType.ORGANISATION_UNIT );
    }

    public Uid getTrackerProgram()
    {
        return trackerProgram;
    }

    public void setTrackerProgram( String trackerProgram )
    {
        this.trackerProgram = new Uid( trackerProgram, MetadataType.PROGRAM );
    }

    public Uid getTrackedEntityType()
    {
        return trackedEntityType;
    }

    public void setTrackedEntityType( String trackedEntityType )
    {
        this.trackedEntityType = new Uid( trackedEntityType, MetadataType.TRACKED_ENTITY_TYPE );
    }

    public Uid getTrackedEntityInstance()
    {
        return trackedEntityInstance;
    }

    public void setTrackedEntityInstance( String trackedEntityInstance )
    {
        this.trackedEntityInstance = new Uid( trackedEntityInstance, MetadataType.TRACKED_ENTITY_INSTANCE );
    }

    public Uid getEnrollment()
    {
        return enrollment;
    }

    public void setEnrollment( String enrollment )
    {
        this.enrollment = new Uid( enrollment, MetadataType.ENROLLMENT );
    }

    public Date getEnrollmentDate()
    {
        return enrollmentDate;
    }

    public void setEnrollmentDate( Date enrollmentDate )
    {
        this.enrollmentDate = enrollmentDate;
    }

    public SmsEnrollmentStatus getEnrollmentStatus()
    {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus( SmsEnrollmentStatus enrollmentStatus )
    {
        this.enrollmentStatus = enrollmentStatus;
    }

    public Date getIncidentDate()
    {
        return incidentDate;
    }

    public void setIncidentDate( Date incidentDate )
    {
        this.incidentDate = incidentDate;
    }

    public GeoPoint getCoordinates()
    {
        return coordinates;
    }

    public void setCoordinates( GeoPoint coordinates )
    {
        this.coordinates = coordinates;
    }

    public List<SmsAttributeValue> getValues()
    {
        return values;
    }

    public void setValues( List<SmsAttributeValue> values )
    {
        this.values = values;
    }

    public List<SmsEvent> getEvents()
    {
        return events;
    }

    public void setEvents( List<SmsEvent> events )
    {
        this.events = events;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( !super.equals( o ) )
        {
            return false;
        }
        EnrollmentSmsSubmission subm = (EnrollmentSmsSubmission) o;

        return Objects.equals( orgUnit, subm.orgUnit ) && Objects.equals( trackerProgram, subm.trackerProgram )
            && Objects.equals( trackedEntityType, subm.trackedEntityType )
            && Objects.equals( trackedEntityInstance, subm.trackedEntityInstance )
            && Objects.equals( enrollment, subm.enrollment ) && Objects.equals( enrollmentDate, subm.enrollmentDate )
            && Objects.equals( enrollmentStatus, subm.enrollmentStatus )
            && Objects.equals( incidentDate, subm.incidentDate ) && Objects.equals( coordinates, subm.coordinates )
            && Objects.equals( values, subm.values ) && Objects.equals( events, subm.events );
    }

    @Override
    public void writeSubm( SmsSubmissionWriter writer, int version )
        throws SmsCompressionException
    {
        switch ( version )
        {
        case 1:
            writeSubmV1( writer, version );
            break;
        case 2:
            writeSubmV2( writer, version );
            break;
        default:
            throw new SmsCompressionException( versionError( version ) );
        }
    }

    private void writeSubmV1( SmsSubmissionWriter writer, int version )
        throws SmsCompressionException
    {
        writer.writeId( orgUnit );
        writer.writeId( trackerProgram );
        writer.writeId( trackedEntityType );
        writer.writeId( trackedEntityInstance );
        writer.writeId( enrollment );
        writer.writeNonNullableDate( enrollmentDate );
        writer.writeAttributeValues( values );
    }

    private void writeSubmV2( SmsSubmissionWriter writer, int version )
        throws SmsCompressionException
    {
        writer.writeId( orgUnit );
        writer.writeId( trackerProgram );
        writer.writeId( trackedEntityType );
        writer.writeId( trackedEntityInstance );
        writer.writeId( enrollment );
        writer.writeDate( enrollmentDate );
        writer.writeEnrollmentStatus( enrollmentStatus );
        writer.writeDate( incidentDate );
        writer.writeGeoPoint( coordinates );
        boolean hasValues = (values != null && !values.isEmpty());
        writer.writeBool( hasValues );
        if ( hasValues )
        {
            writer.writeAttributeValues( values );
        }
        writer.writeEvents( events, version );
    }

    @Override
    public void readSubm( SmsSubmissionReader reader, int version )
        throws SmsCompressionException
    {
        switch ( version )
        {
        case 1:
            readSubmV1( reader, version );
            break;
        case 2:
            readSubmV2( reader, version );
            break;
        default:
            throw new SmsCompressionException( versionError( version ) );
        }
    }

    public void readSubmV1( SmsSubmissionReader reader, int version )
        throws SmsCompressionException
    {
        this.orgUnit = reader.readId( MetadataType.ORGANISATION_UNIT );
        this.trackerProgram = reader.readId( MetadataType.PROGRAM );
        this.trackedEntityType = reader.readId( MetadataType.TRACKED_ENTITY_TYPE );
        this.trackedEntityInstance = reader.readId( MetadataType.TRACKED_ENTITY_INSTANCE );
        this.enrollment = reader.readId( MetadataType.ENROLLMENT );
        this.enrollmentDate = reader.readNonNullableDate();
        this.values = reader.readAttributeValues();
        this.events = null;
    }

    public void readSubmV2( SmsSubmissionReader reader, int version )
        throws SmsCompressionException
    {
        this.orgUnit = reader.readId( MetadataType.ORGANISATION_UNIT );
        this.trackerProgram = reader.readId( MetadataType.PROGRAM );
        this.trackedEntityType = reader.readId( MetadataType.TRACKED_ENTITY_TYPE );
        this.trackedEntityInstance = reader.readId( MetadataType.TRACKED_ENTITY_INSTANCE );
        this.enrollment = reader.readId( MetadataType.ENROLLMENT );
        this.enrollmentDate = reader.readDate();
        this.enrollmentStatus = reader.readEnrollmentStatus();
        this.incidentDate = reader.readDate();
        this.coordinates = reader.readGeoPoint();
        boolean hasValues = reader.readBool();
        this.values = hasValues ? reader.readAttributeValues() : null;
        this.events = reader.readEvents( version );
    }

    @Override
    public int getCurrentVersion()
    {
        return 2;
    }

    @Override
    public SubmissionType getType()
    {
        return SmsConsts.SubmissionType.ENROLLMENT;
    }

}

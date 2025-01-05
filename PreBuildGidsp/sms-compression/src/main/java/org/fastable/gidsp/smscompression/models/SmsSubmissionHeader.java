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

import org.fastable.gidsp.smscompression.SmsCompressionException;
import org.fastable.gidsp.smscompression.SmsConsts.SubmissionType;
import org.fastable.gidsp.smscompression.SmsSubmissionReader;
import org.fastable.gidsp.smscompression.SmsSubmissionWriter;

public class SmsSubmissionHeader
{
    protected SubmissionType type;

    protected int version;

    protected Date lastSyncDate;

    protected int submissionId;

    public int getSubmissionId()
    {
        return submissionId;
    }

    public void setSubmissionId( int submissionId )
    {
        this.submissionId = submissionId;
    }

    public SubmissionType getType()
    {
        return type;
    }

    public void setType( SubmissionType type )
    {
        this.type = type;
    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion( int version )
    {
        this.version = version;
    }

    public Date getLastSyncDate()
    {
        return lastSyncDate;
    }

    public void setLastSyncDate( Date lastSyncDate )
    {
        this.lastSyncDate = lastSyncDate;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        SmsSubmissionHeader hdr = (SmsSubmissionHeader) o;
        return type.equals( hdr.type ) && version == hdr.version && lastSyncDate.equals( hdr.lastSyncDate )
            && submissionId == hdr.submissionId;
    }

    public void validateHeaer()
        throws SmsCompressionException
    {
        // TODO: More validation here
        if ( submissionId < 0 || submissionId > 255 )
        {
            throw new SmsCompressionException( "Ensure the Submission ID has been set for this submission" );
        }
    }

    public void writeHeader( SmsSubmissionWriter writer )
        throws SmsCompressionException
    {
        writer.writeType( type );
        writer.writeVersion( version );
        writer.writeNonNullableDate( lastSyncDate );
        writer.writeSubmissionId( submissionId );
    }

    public void readHeader( SmsSubmissionReader reader )
        throws SmsCompressionException
    {
        this.type = reader.readType();
        this.version = reader.readVersion();
        this.lastSyncDate = reader.readNonNullableDate();
        this.submissionId = reader.readSubmissionId();
    }
}

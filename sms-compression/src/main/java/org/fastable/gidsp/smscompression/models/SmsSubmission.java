package org.fastable.gidsp.smscompression.models;

import java.util.Date;

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

import org.fastable.gidsp.smscompression.SmsCompressionException;
import org.fastable.gidsp.smscompression.SmsConsts.MetadataType;
import org.fastable.gidsp.smscompression.SmsConsts.SubmissionType;
import org.fastable.gidsp.smscompression.SmsSubmissionReader;
import org.fastable.gidsp.smscompression.SmsSubmissionWriter;

public abstract class SmsSubmission
{
    protected SmsSubmissionHeader header;

    protected Uid userId;

    public abstract int getCurrentVersion();

    public abstract SubmissionType getType();

    // Note: When handling versioning, create a new method to handle
    // each version, rather than handling all formats in this method alone
    public abstract void writeSubm( SmsSubmissionWriter writer, int version )
        throws SmsCompressionException;

    public abstract void readSubm( SmsSubmissionReader reader, int version )
        throws SmsCompressionException;

    public SmsSubmission()
    {
        this.header = new SmsSubmissionHeader();
        header.setType( this.getType() );
        // Initialise the submission ID so we know if it's been set correctly
        header.setSubmissionId( -1 );
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
        SmsSubmission subm = (SmsSubmission) o;
        return userId.equals( subm.userId ) && header.equals( subm.header );
    }

    public void setSubmissionId( int submissionId )
    {
        header.setSubmissionId( submissionId );
    }

    public Uid getUserId()
    {
        return userId;
    }

    public void setUserId( String userId )
    {
        this.userId = new Uid( userId, MetadataType.USER );
    }

    public void validateSubmission()
        throws SmsCompressionException
    {
        header.validateHeaer();
        if ( userId.getUid().isEmpty() )
        {
            throw new SmsCompressionException( "Ensure the UserID is set in the submission" );
        }
        // TODO: We should run validations on each submission here
    }

    public void write( SmsMetadata meta, SmsSubmissionWriter writer, int version )
        throws SmsCompressionException
    {
        // Ensure we set the lastSyncDate in the subm header
        Date lastSyncDate = meta != null && meta.lastSyncDate != null ? meta.lastSyncDate : new Date( 0 );
        header.setLastSyncDate( lastSyncDate );

        validateSubmission();
        header.setVersion( version );
        header.writeHeader( writer );
        writer.writeId( userId );
        writeSubm( writer, version );
    }

    public void read( SmsSubmissionReader reader, SmsSubmissionHeader header )
        throws SmsCompressionException
    {
        this.header = header;
        this.userId = reader.readId( MetadataType.USER );
        readSubm( reader, this.header.getVersion() );
    }

    protected String versionError( int version )
    {
        return String.format( "Version %d of %s is not supported", version, this.getClass().getSimpleName() );
    }
}
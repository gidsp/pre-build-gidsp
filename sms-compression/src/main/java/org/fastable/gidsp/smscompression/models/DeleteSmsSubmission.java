package org.fastable.gidsp.smscompression.models;

import java.util.Objects;

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
import org.fastable.gidsp.smscompression.SmsConsts;
import org.fastable.gidsp.smscompression.SmsConsts.MetadataType;
import org.fastable.gidsp.smscompression.SmsConsts.SubmissionType;
import org.fastable.gidsp.smscompression.SmsSubmissionReader;
import org.fastable.gidsp.smscompression.SmsSubmissionWriter;

public class DeleteSmsSubmission
    extends
    SmsSubmission
{
    protected Uid event;

    /* Getters and Setters */

    public Uid getEvent()
    {
        return event;
    }

    public void setEvent( String event )
    {
        this.event = new Uid( event, MetadataType.EVENT );
    }

    @Override
    public boolean equals( Object o )
    {
        if ( !super.equals( o ) )
        {
            return false;
        }
        DeleteSmsSubmission subm = (DeleteSmsSubmission) o;
        return Objects.equals( event, subm.event );
    }

    /* Implementation of abstract methods */

    @Override
    public void writeSubm( SmsSubmissionWriter writer, int version )
        throws SmsCompressionException
    {
        if ( version != 1 && version != 2 )
        {
            throw new SmsCompressionException( versionError( version ) );
        }
        writer.writeId( event );
    }

    @Override
    public void readSubm( SmsSubmissionReader reader, int version )
        throws SmsCompressionException
    {
        if ( version != 1 && version != 2 )
        {
            throw new SmsCompressionException( versionError( version ) );
        }
        this.event = reader.readId( MetadataType.EVENT );
    }

    @Override
    public int getCurrentVersion()
    {
        return 2;
    }

    @Override
    public SubmissionType getType()
    {
        return SmsConsts.SubmissionType.DELETE;
    }
}

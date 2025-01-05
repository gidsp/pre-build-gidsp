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

public class RelationshipSmsSubmission
    extends
    SmsSubmission
{
    protected Uid relationshipType;

    protected Uid relationship;

    protected Uid from;

    protected Uid to;

    /* Getters and Setters */

    public Uid getRelationshipType()
    {
        return relationshipType;
    }

    public void setRelationshipType( String relationshipType )
    {
        this.relationshipType = new Uid( relationshipType, MetadataType.RELATIONSHIP_TYPE );
    }

    public Uid getRelationship()
    {
        return relationship;
    }

    public void setRelationship( String relationship )
    {
        this.relationship = new Uid( relationship, MetadataType.RELATIONSHIP );
    }

    public Uid getFrom()
    {
        return from;
    }

    public void setFrom( String from )
    {
        this.from = new Uid( from, null );
    }

    public Uid getTo()
    {
        return to;
    }

    public void setTo( String to )
    {
        this.to = new Uid( to, null );
    }

    @Override
    public boolean equals( Object o )
    {
        if ( !super.equals( o ) )
        {
            return false;
        }
        RelationshipSmsSubmission subm = (RelationshipSmsSubmission) o;

        return Objects.equals( relationshipType, subm.relationshipType )
            && Objects.equals( relationship, subm.relationship ) && Objects.equals( from, subm.from )
            && Objects.equals( to, subm.to );
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
        writer.writeId( relationshipType );
        writer.writeId( relationship );
        writer.writeNewId( from.getUid() );
        writer.writeNewId( to.getUid() );
    }

    @Override
    public void readSubm( SmsSubmissionReader reader, int version )
        throws SmsCompressionException
    {
        if ( version != 1 && version != 2 )
        {
            throw new SmsCompressionException( versionError( version ) );
        }
        this.relationshipType = reader.readId( MetadataType.RELATIONSHIP_TYPE );
        this.relationship = reader.readId( MetadataType.RELATIONSHIP );
        this.from = new Uid( reader.readNewId(), null );
        this.to = new Uid( reader.readNewId(), null );
    }

    @Override
    public int getCurrentVersion()
    {
        return 2;
    }

    @Override
    public SubmissionType getType()
    {
        return SmsConsts.SubmissionType.RELATIONSHIP;
    }
}

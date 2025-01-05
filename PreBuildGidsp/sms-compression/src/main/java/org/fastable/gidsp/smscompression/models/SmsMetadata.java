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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.fastable.gidsp.smscompression.SmsCompressionException;
import org.fastable.gidsp.smscompression.SmsConsts.MetadataType;
import org.fastable.gidsp.smscompression.utils.IdUtil;

public class SmsMetadata
{
    public static final List<MetadataType> ValidHashTypes = Collections
        .unmodifiableList( Arrays.asList( MetadataType.USER, MetadataType.TRACKED_ENTITY_TYPE,
            MetadataType.TRACKED_ENTITY_ATTRIBUTE, MetadataType.PROGRAM, MetadataType.ORGANISATION_UNIT,
            MetadataType.DATA_ELEMENT, MetadataType.CATEGORY_OPTION_COMBO, MetadataType.DATASET,
            MetadataType.PROGRAM_STAGE, MetadataType.RELATIONSHIP_TYPE ) );

    public static class Id
    {
        String id;

        public Id( String id )
        {
            this.id = id;
        }
    }

    public Date lastSyncDate;

    public List<Id> users;

    public List<Id> trackedEntityTypes;

    public List<Id> trackedEntityAttributes;

    public List<Id> programs;

    public List<Id> organisationUnits;

    public List<Id> dataElements;

    public List<Id> categoryOptionCombos;

    public List<Id> dataSets;

    public List<Id> programStages;

    public List<Id> relationshipTypes;

    public void validate()
        throws SmsCompressionException
    {
        for ( MetadataType type : ValidHashTypes )
        {
            checkIdList( getType( type ), type );
        }
    }

    public static boolean checkIdList( List<String> ids, MetadataType type )
        throws SmsCompressionException
    {
        String typeMsg = "Metadata error[" + type + "]:";
        HashSet<String> set = new HashSet<>();
        for ( String id : ids )
        {
            if ( !set.add( id ) )
                throw new SmsCompressionException( typeMsg + "List of UIDs in Metadata contains duplicate: " + id );
            if ( !IdUtil.validId( id ) )
                throw new SmsCompressionException( typeMsg + "Invalid format UID found in Metadata UID List: " + id );
        }

        return true;
    }

    public List<String> getType( MetadataType type )
    {
        switch ( type )
        {
        case USER:
            return getIds( users );
        case TRACKED_ENTITY_TYPE:
            return getIds( trackedEntityTypes );
        case TRACKED_ENTITY_ATTRIBUTE:
            return getIds( trackedEntityAttributes );
        case PROGRAM:
            return getIds( programs );
        case ORGANISATION_UNIT:
            return getIds( organisationUnits );
        case DATA_ELEMENT:
            return getIds( dataElements );
        case CATEGORY_OPTION_COMBO:
            return getIds( categoryOptionCombos );
        case DATASET:
            return getIds( dataSets );
        case PROGRAM_STAGE:
            return getIds( programStages );
        case RELATIONSHIP_TYPE:
            return getIds( relationshipTypes );

        default:
            return null;
        }
    }

    private List<String> getIds( List<Id> ids )
    {
        ArrayList<String> idList = new ArrayList<>();

        if ( ids != null )
        {
            for ( Id id : ids )
            {
                idList.add( id.id );
            }
        }

        return idList;
    }
}

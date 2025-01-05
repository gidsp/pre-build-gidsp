package org.fastable.gidsp.smscompression.models;

import org.fastable.gidsp.smscompression.SmsConsts.MetadataType;

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

public class SmsDataValue
{
    protected Uid categoryOptionCombo;

    protected Uid dataElement;

    protected String value;

    protected SmsValue<?> smsValue;

    public SmsDataValue( String categoryOptionCombo, String dataElement, String value )
    {
        this.categoryOptionCombo = new Uid( categoryOptionCombo, MetadataType.CATEGORY_OPTION_COMBO );
        this.dataElement = new Uid( dataElement, MetadataType.DATA_ELEMENT );
        this.value = value;
        this.smsValue = SmsValue.asSmsValue( value );
    }

    public SmsDataValue( Uid categoryOptionCombo, Uid dataElement, SmsValue<?> smsValue )
    {
        this.categoryOptionCombo = categoryOptionCombo;
        this.dataElement = dataElement;
        this.smsValue = smsValue;
        // TODO: We probably need better handling than just toString() here
        this.value = smsValue.getValue().toString();
    }

    public Uid getCategoryOptionCombo()
    {
        return categoryOptionCombo;
    }

    public Uid getDataElement()
    {
        return dataElement;
    }

    public String getValue()
    {
        return value;
    }

    public SmsValue<?> getSmsValue()
    {
        return smsValue;
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
        SmsDataValue dv = (SmsDataValue) o;

        return categoryOptionCombo.equals( dv.categoryOptionCombo ) && dataElement.equals( dv.dataElement )
            && value.equals( dv.value );
    }

    @Override
    public int hashCode()
    {
        return 0;
    }
}

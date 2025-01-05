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

import java.util.List;
import java.util.Objects;

import org.fastable.gidsp.smscompression.SmsCompressionException;
import org.fastable.gidsp.smscompression.SmsConsts;
import org.fastable.gidsp.smscompression.SmsConsts.MetadataType;
import org.fastable.gidsp.smscompression.SmsConsts.SubmissionType;
import org.fastable.gidsp.smscompression.SmsSubmissionReader;
import org.fastable.gidsp.smscompression.SmsSubmissionWriter;

public class AggregateDatasetSmsSubmission
    extends
    SmsSubmission
{
    protected Uid orgUnit;

    protected Uid dataSet;

    protected boolean complete;

    protected Uid attributeOptionCombo;

    protected String period;

    protected List<SmsDataValue> values;

    /* Getters and Setters */

    public Uid getOrgUnit()
    {
        return orgUnit;
    }

    public void setOrgUnit( String orgUnit )
    {
        this.orgUnit = new Uid( orgUnit, MetadataType.ORGANISATION_UNIT );
    }

    public Uid getDataSet()
    {
        return dataSet;
    }

    public void setDataSet( String dataSet )
    {
        this.dataSet = new Uid( dataSet, MetadataType.DATASET );
    }

    public boolean isComplete()
    {
        return complete;
    }

    public void setComplete( boolean complete )
    {
        this.complete = complete;
    }

    public Uid getAttributeOptionCombo()
    {
        return attributeOptionCombo;
    }

    public void setAttributeOptionCombo( String attributeOptionCombo )
    {
        this.attributeOptionCombo = new Uid( attributeOptionCombo, MetadataType.CATEGORY_OPTION_COMBO );
    }

    public String getPeriod()
    {
        return period;
    }

    public void setPeriod( String period )
    {
        this.period = period;
    }

    public List<SmsDataValue> getValues()
    {
        return values;
    }

    public void setValues( List<SmsDataValue> values )
    {
        this.values = values;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( !super.equals( o ) )
        {
            return false;
        }
        AggregateDatasetSmsSubmission subm = (AggregateDatasetSmsSubmission) o;

        return Objects.equals( orgUnit, subm.orgUnit ) && Objects.equals( dataSet, subm.dataSet )
            && Objects.equals( complete, subm.complete )
            && Objects.equals( attributeOptionCombo, subm.attributeOptionCombo )
            && Objects.equals( period, subm.period ) && Objects.equals( values, subm.values );
    }

    /* Implementation of abstract methods */

    @Override
    public void writeSubm( SmsSubmissionWriter writer, int version )
        throws SmsCompressionException
    {
        switch ( version )
        {
        case 1:
            writeSubmV1( writer );
            break;
        case 2:
            writeSubmV2( writer );
            break;
        default:
            throw new SmsCompressionException( versionError( version ) );
        }
    }

    private void writeSubmV1( SmsSubmissionWriter writer )
        throws SmsCompressionException
    {
        writer.writeId( orgUnit );
        writer.writeId( dataSet );
        writer.writeBool( complete );
        writer.writeId( attributeOptionCombo );
        writer.writePeriod( period );
        writer.writeDataValues( values );
    }

    private void writeSubmV2( SmsSubmissionWriter writer )
        throws SmsCompressionException
    {
        writer.writeId( orgUnit );
        writer.writeId( dataSet );
        writer.writeBool( complete );
        writer.writeId( attributeOptionCombo );
        writer.writePeriod( period );
        boolean hasValues = (values != null && !values.isEmpty());
        writer.writeBool( hasValues );
        if ( hasValues )
        {
            writer.writeDataValues( values );
        }
    }

    @Override
    public void readSubm( SmsSubmissionReader reader, int version )
        throws SmsCompressionException
    {
        switch ( version )
        {
        case 1:
            readSubmV1( reader );
            break;
        case 2:
            readSubmV2( reader );
            break;
        default:
            throw new SmsCompressionException( versionError( version ) );
        }
    }

    private void readSubmV1( SmsSubmissionReader reader )
        throws SmsCompressionException
    {
        this.orgUnit = reader.readId( MetadataType.ORGANISATION_UNIT );
        this.dataSet = reader.readId( MetadataType.DATASET );
        this.complete = reader.readBool();
        this.attributeOptionCombo = reader.readId( MetadataType.CATEGORY_OPTION_COMBO );
        this.period = reader.readPeriod();
        this.values = reader.readDataValues();
    }

    private void readSubmV2( SmsSubmissionReader reader )
        throws SmsCompressionException
    {
        this.orgUnit = reader.readId( MetadataType.ORGANISATION_UNIT );
        this.dataSet = reader.readId( MetadataType.DATASET );
        this.complete = reader.readBool();
        this.attributeOptionCombo = reader.readId( MetadataType.CATEGORY_OPTION_COMBO );
        this.period = reader.readPeriod();
        boolean hasValues = reader.readBool();
        this.values = hasValues ? reader.readDataValues() : null;
    }

    @Override
    public int getCurrentVersion()
    {
        return 2;
    }

    @Override
    public SubmissionType getType()
    {
        return SmsConsts.SubmissionType.AGGREGATE_DATASET;
    }
}

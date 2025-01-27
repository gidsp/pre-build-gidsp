package org.fastable.quick;

import javax.sql.DataSource;

/*
 * Copyright (c) 2004-2016, General Intergrate Date Service Platform
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

/**
 * Represents configuration for a JDBC database connection.
 *
 * @author Lars Helge Overland
 */
public class JdbcConfiguration
{
    private StatementDialect dialect;

    private DataSource dataSource;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public JdbcConfiguration()
    {
    }

    public JdbcConfiguration( StatementDialect dialect, DataSource dataSource )
    {
        this.dialect = dialect;
        this.dataSource = dataSource;
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString()
    {
        return "[Dialect: " + dialect + ", dataSource runtime class: " + dataSource.getClass().getName() + "]";
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public StatementDialect getDialect()
    {
        return dialect;
    }

    public void setDialect( StatementDialect dialect )
    {
        this.dialect = dialect;
    }

    public DataSource getDataSource()
    {
        return dataSource;
    }

    public void setDataSource( DataSource dataSource )
    {
        this.dataSource = dataSource;
    }

}

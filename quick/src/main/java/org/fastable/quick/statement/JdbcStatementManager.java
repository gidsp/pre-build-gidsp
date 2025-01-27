package org.fastable.quick.statement;

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

import java.sql.Connection;
import java.sql.SQLException;

import org.fastable.quick.JdbcConfiguration;
import org.fastable.quick.StatementHolder;
import org.fastable.quick.StatementManager;

/**
 * JDBC implementation of the StatementManager insterface.
 *
 * @author Lars Helge Overland
 */
public class JdbcStatementManager
    implements StatementManager
{
    private ThreadLocal<StatementHolder> holderTag = new ThreadLocal<>();

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private JdbcConfiguration jdbcConfiguration;

    public void setJdbcConfiguration( JdbcConfiguration jdbcConfiguration )
    {
        this.jdbcConfiguration = jdbcConfiguration;
    }

    // -------------------------------------------------------------------------
    // StatementManager implementation
    // -------------------------------------------------------------------------

    @Override
    public void initialise()
    {
        Connection connection = getConnection();

        StatementHolder holder = new DefaultStatementHolder( connection, true );

        holderTag.set( holder );
    }

    @Override
    public StatementHolder getHolder()
    {
        StatementHolder holder = holderTag.get();

        if ( holder != null )
        {
            return holder;
        }

        return new DefaultStatementHolder( getConnection(), false );
    }

    @Override
    public StatementHolder getHolder( boolean autoCommit )
    {
        try
        {
            Connection connection = getConnection();
            connection.setAutoCommit( autoCommit );
            return new DefaultStatementHolder( connection );
        }
        catch ( SQLException ex )
        {
            destroy();

            throw new RuntimeException( ex );
        }
    }

    @Override
    public void destroy()
    {
        StatementHolder holder = holderTag.get();

        if ( holder != null )
        {
            holder.forceClose();

            holderTag.remove();
        }
    }

    @Override
    public JdbcConfiguration getConfiguration()
    {
        return jdbcConfiguration;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private Connection getConnection()
    {
        return getConnection( getConfiguration() );
    }

    private Connection getConnection( JdbcConfiguration config )
    {
        try
        {
            Connection connection = config.getDataSource().getConnection();

            return connection;
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to create connection", ex );
        }
    }
}

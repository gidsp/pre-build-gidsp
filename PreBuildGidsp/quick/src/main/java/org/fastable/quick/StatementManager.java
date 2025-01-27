package org.fastable.quick;

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
 * Interface which provides methods for retrieving StatementHolder interfaces.
 *
 * @author Lars Helge Overland
 */
public interface StatementManager
{
    /**
     * Initalizes a database connection and statement object which can be
     * re-used for multiple operations.
     */
    void initialise();

    /**
     * Gets a pre-initialized StatementHolder object. After the initialize()
     * method is invoked, the StatementHolder provides connection pooling in the
     * sense that the same underlying Connection and Statement will be used
     * until destroy() is invoked.
     *
     * @return a pre-initialized statement object.
     */
    StatementHolder getHolder();

    /**
     * Gets a pre-initalized StatementHolder object. This method will always
     * retrieve a new Connection from the database, disregard of initialization.
     *
     * @param autoCommit turn auto-commit on or off for the underlying
     *        Connection.
     * @return a pre-initialized statement object.
     */
    StatementHolder getHolder( boolean autoCommit );

    /**
     * Closes the StatementHolder object and the underlying database connection.
     */
    void destroy();

    /**
     * Returns the current JdbcConfiguration.
     *
     * @return JDBC configuration.
     */
    JdbcConfiguration getConfiguration();
}

package org.fastable.gidsp.smscompression.utils;

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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.fastable.gidsp.smscompression.SmsCompressionException;

/**
 * A stream of bits that can be read. Because they come from an underlying byte
 * stream, the total number of bits is always a multiple of 8. The bits are read
 * in big endian. Mutable and not thread-safe.
 * 
 * @see BitOutputStream
 */
public final class BitInputStream
    implements
    AutoCloseable
{
    private InputStream input;

    private int currentByte;

    private int numBitsRemaining;

    public BitInputStream( InputStream in )
    {
        Objects.requireNonNull( in );
        input = in;
        currentByte = 0;
        numBitsRemaining = 0;
    }

    /**
     * Reads a bit from this stream. Returns 0 or 1 if a bit is available, or -1
     * if the end of stream is reached. The end of stream always occurs on a
     * byte boundary.
     * 
     * @return the next bit of 0 or 1, or -1 for the end of stream
     * @throws IOException if an I/O exception occurred
     */
    public int readBit()
        throws IOException
    {
        if ( currentByte == -1 )
            return -1;
        if ( numBitsRemaining == 0 )
        {
            currentByte = input.read();
            if ( currentByte == -1 )
                return -1;
            numBitsRemaining = 8;
        }
        if ( numBitsRemaining <= 0 )
            throw new AssertionError();
        numBitsRemaining--;
        return (currentByte >>> numBitsRemaining) & 1;
    }

    /**
     * Reads n bits from this stream and returns the result as an integer.
     * Throws an EOFException if the end of the stream is reached.
     * 
     * @return the next n bits as an integer
     * @throws SmsCompressionException if an I/O exception or EOF exception
     *         occurred
     */
    public int read( int n )
        throws SmsCompressionException
    {
        try
        {
            int i = 0;
            while ( n > 0 )
            {
                i <<= 1;
                i |= readBitEof();
                n--;
            }
            return i;
        }
        catch ( IOException e )
        {
            throw new SmsCompressionException( e );
        }
    }

    /**
     * Reads a bit from this stream. Returns 0 or 1 if a bit is available, or
     * throws an {@code EOFException} if the end of stream is reached. The end
     * of stream always occurs on a byte boundary.
     * 
     * @return the next bit, 0 or 1
     * @throws IOException if an I/O exception occurred
     * @throws EOFException if the end of stream is reached
     */
    public int readBitEof()
        throws IOException
    {
        int result = readBit();
        if ( result != -1 )
            return result;
        else
            throw new EOFException();
    }

    /**
     * Closes this stream and the underlying input stream.
     * 
     * @throws IOException if an I/O exception occurred
     */
    @Override
    public void close()
        throws IOException
    {
        input.close();
        currentByte = -1;
        numBitsRemaining = 0;
    }

}

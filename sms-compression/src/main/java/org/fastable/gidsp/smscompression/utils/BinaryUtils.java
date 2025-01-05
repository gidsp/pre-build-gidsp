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

public class BinaryUtils
{

    /**
     * @return an array of bytes as a binary string
     */
    public static String print( byte[] byteArray )
    {
        String output = "";
        for ( byte b : byteArray )
        {
            output += print( b );
        }

        return output;
    }

    /**
     * @return a single byte as a binary string
     */
    public static String print( byte b )
    {
        return Integer.toBinaryString( (b & 0xFF) + 0x100 ).substring( 1 );
    }

    /**
     * @return the log2 of n
     */
    public static int log2( int n )
    {
        if ( n <= 0 )
            throw new IllegalArgumentException();
        return 31 - Integer.numberOfLeadingZeros( n );
    }

    /**
     * @return the minimum bit length needed to represent this int (unsigned)
     */
    public static int bitlenNeeded( int n )
    {
        return n == 0 ? 1 : log2( n ) + 1;
    }

    /**
     * @return a unique hash for a string for a given bit length
     */
    public static int hash( String s, int bitlen )
    {
        return Math.abs( s.hashCode() ) % (int) Math.pow( 2, bitlen );
    }
}

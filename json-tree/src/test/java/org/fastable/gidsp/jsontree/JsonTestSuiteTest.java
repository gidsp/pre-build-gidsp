/*
 * Copyright (c) 2004-2023, General Intergrate Date Service Platform
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
package org.fastable.gidsp.jsontree;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

/**
 * Test scenarios taken from the
 * <a href="https://github.com/nst/JSONTestSuite">JSONTestSuite</a>.
 * <p>
 * Test method names are same as the test input file names of the suite.
 *
 * @author Jan Bernitt
 */
class JsonTestSuiteTest {

    @Test
    void y_object_duplicated_key() {
        JsonValue value = assert_y( "{\"a\":\"b\",\"a\":\"c\"}" );
        assertEquals( "b", value.asObject().getString( "a" ).string() );
    }

    @Test
    void y_object_duplicated_key_and_value() {
        JsonValue value = assert_y( "{\"a\":\"b\",\"a\":\"b\"}" );
        assertEquals( "b", value.asObject().getString( "a" ).string() );
    }

    @Test
    void y_array_with_trailing_space() {
        assert_y( "[2] " );
    }

    @Test
    void y_number_double_close_to_zero() {
        assert_y( "[-0.000000000000000000000000000000000000000000000000000000000000000000000000000001]" );
    }

    @Test
    void y_structure_whitespace_array() {
        assert_y( " []" );
    }

    @Test
    void n_object_trailing_comment_slash_open() {
        assert_n( "{\"a\":\"b\"}//" );
    }

    @Test
    void n_array_1_true_without_comma() {
        assert_n( "[1 true]" );
    }

    @Test
    void n_array_comma_after_close() {
        assert_n( "[\"\"]," );
    }

    @Test
    void n_number_0e() {
        assert_n( "[0e]" );
    }

    @Test
    void n_number_expression() {
        assert_n( "[1+2]" );
    }

    @Test
    void n_number_2_e3() {
        assert_n( "[2.e3]" );
    }

    @Test
    void n_structure_no_data() {
        assertThrowsExactly( JsonFormatException.class, () -> JsonNode.of( "" ) );
    }

    @Test
    void n_structure_single_star() {
        assertThrowsExactly( JsonFormatException.class, () -> JsonNode.of( "*" ) );
        assert_n( "[*]" );
    }

    @Test
    void n_string_single_doublequote() {
        JsonNode node = JsonNode.of( "\"" );
        assertThrowsExactly( JsonFormatException.class, node::value );
    }

    /*
     * Helpers
     */

    private JsonValue assert_y( String json ) {
        JsonValue value = JsonValue.of( json );
        assert_y( value );
        return value;
    }

    private void assert_y( JsonValue value ) {
        value.node().visit( n -> {
        } );
    }

    private void assert_n( String json ) {
        assert_n( JsonValue.of( json ) );
    }

    private void assert_n( JsonValue value ) {
        assert_n( value.node() );
    }

    private void assert_n( JsonNode node ) {
        assertThrowsExactly( JsonFormatException.class, () -> node.visit( n -> {
        } ) );
    }
}

/*
 * Copyright (c) 2004-2022, General Intergrate Date Service Platform
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the static helpers of {@link JsonValue} and the basic {@link JsonValue} API methods.
 *
 * @author Jan Bernitt
 */
class JsonValueTest {

    @Test
    void testOfJsonNode() {
        JsonNode node = JsonBuilder.createObject( obj -> obj.addString( "foo", "bar" ) );
        JsonValue value = JsonValue.of( node );
        assertNotNull( value );
        assertEquals( "{\"foo\":\"bar\"}", value.toString() );
        assertSame( node, value.node() );
    }

    @Test
    void testAsType() {
        JsonValue value = JsonMixed.of( "\"http://example.com\"" );
        assertSame( JsonMixed.class, value.asType() );
        assertSame( JsonMixed.class, value.as( JsonArray.class ).asType() );
        assertSame( JsonURL.class, value.as( JsonURL.class ).asType() );
        assertSame( JsonMixed.class, value.as( JsonURL.class ).as( JsonValue.class ).asType() );
    }

    @Test
    void testIsObject() {
        assertFalse( JsonValue.of( "1" ).isObject() );
        assertFalse( JsonValue.of( "true" ).isObject() );
        assertFalse( JsonValue.of( "null" ).isObject() );
        assertFalse( JsonValue.of( "[]" ).isObject() );
        assertFalse( JsonValue.of( "\"str\"" ).isObject() );
        assertTrue( JsonValue.of( "{}" ).isObject() );
    }

    @Test
    void testIsArray() {
        assertFalse( JsonValue.of( "1" ).isArray() );
        assertFalse( JsonValue.of( "true" ).isArray() );
        assertFalse( JsonValue.of( "null" ).isArray() );
        assertTrue( JsonValue.of( "[]" ).isArray() );
        assertFalse( JsonValue.of( "\"str\"" ).isArray() );
        assertFalse( JsonValue.of( "{}" ).isArray() );
    }

    @Test
    void testIsNumber() {
        assertTrue( JsonValue.of( "1" ).isNumber() );
        assertFalse( JsonValue.of( "true" ).isNumber() );
        assertFalse( JsonValue.of( "null" ).isNumber() );
        assertFalse( JsonValue.of( "[]" ).isNumber() );
        assertFalse( JsonValue.of( "\"str\"" ).isNumber() );
        assertFalse( JsonValue.of( "{}" ).isNumber() );
    }

    @Test
    void testIsString() {
        assertFalse( JsonValue.of( "1" ).isString() );
        assertFalse( JsonValue.of( "true" ).isString() );
        assertFalse( JsonValue.of( "null" ).isString() );
        assertFalse( JsonValue.of( "[]" ).isString() );
        assertTrue( JsonValue.of( "\"str\"" ).isString() );
        assertFalse( JsonValue.of( "{}" ).isString() );
    }

    @Test
    void testIsBoolean() {
        assertFalse( JsonValue.of( "1" ).isBoolean() );
        assertTrue( JsonValue.of( "true" ).isBoolean() );
        assertFalse( JsonValue.of( "null" ).isBoolean() );
        assertFalse( JsonValue.of( "[]" ).isBoolean() );
        assertFalse( JsonValue.of( "\"str\"" ).isBoolean() );
        assertFalse( JsonValue.of( "{}" ).isBoolean() );
    }

    @Test
    void testIsInteger() {
        assertTrue( JsonValue.of( "1" ).isInteger() );
        assertFalse( JsonValue.of( "true" ).isInteger() );
        assertFalse( JsonValue.of( "null" ).isInteger() );
        assertFalse( JsonValue.of( "[]" ).isInteger() );
        assertFalse( JsonValue.of( "\"str\"" ).isInteger() );
        assertFalse( JsonValue.of( "{}" ).isInteger() );

        assertTrue( JsonValue.of( "1.0" ).isInteger() );
        assertTrue( JsonValue.of( "1.00000" ).isInteger() );
        assertFalse( JsonValue.of( "1.5" ).isInteger() );
        assertFalse( JsonValue.of( "0.4" ).isInteger() );
    }

    @Test
    void testToListFromVarargs_Undefined() {
        assertEquals( List.of(),
            JsonMixed.of( "{}" ).get( "foo" ).toListFromVarargs( JsonNumber.class, JsonNumber::intValue ) );
    }

    @Test
    void testToListFromVarargs_Null() {
        assertEquals( List.of(), JsonValue.of( "null" ).toListFromVarargs( JsonNumber.class, JsonNumber::intValue ) );
    }

    @Test
    void testToListFromVarargs_Simple() {
        assertEquals( List.of( 1 ), JsonValue.of( "1" ).toListFromVarargs( JsonNumber.class, JsonNumber::intValue ) );
    }

    @Test
    void testToListFromVarargs_SimpleWrongType() {
        JsonValue value = JsonValue.of( "true" );
        assertThrowsExactly( JsonTreeException.class,
            () -> value.toListFromVarargs( JsonNumber.class, JsonNumber::intValue ) );
    }

    @Test
    void testToListFromVarargs_ArrayEmpty() {
        assertEquals( List.of(), JsonValue.of( "[]" ).toListFromVarargs( JsonNumber.class, JsonNumber::intValue ) );
    }

    @Test
    void testToListFromVarargs_ArrayNonEmpty() {
        assertEquals( List.of( 1, 2 ),
            JsonValue.of( "[1,2]" ).toListFromVarargs( JsonNumber.class, JsonNumber::intValue ) );

    }

    @Test
    void testToListFromVarargs_ArrayWrongType() {
        JsonValue value = JsonValue.of( "[1,true]" );
        assertThrowsExactly( JsonTreeException.class,
            () -> value.toListFromVarargs( JsonNumber.class, JsonNumber::intValue ) );
    }

    @Test
    void testFind_Object() {
        String json = """
            { "x":{ "foo": 1 }}""";
        JsonMixed root = JsonMixed.of( json );
        JsonObject foo = root.find( JsonObject.class, obj -> obj.has( "foo" ) );
        assertTrue( foo.isObject() );
        assertEquals( "$.x", foo.path() );
        assertFalse( root.find( JsonObject.class, obj -> obj.has( "bar" ) ).exists() );
    }

    @Test
    void testFind_Array() {
        assertEquals( 42, JsonValue.of( "[1,42,99]" )
            .find( JsonNumber.class, n -> n.intValue() > 20 ).intValue() );
    }

    @Test
    void testFind_String() {
        assertFalse( JsonValue.of( "\"hello\"" )
            .find( JsonString.class, n -> n.string().length() > 30 ).exists() );
        assertEquals( "hello", JsonValue.of( "\"hello\"" )
            .find( JsonString.class, n -> n.string().length() > 3 ).string() );
    }

    @Test
    void testFind_Number() {
        assertFalse( JsonMixed.of( "1" ).find( JsonObject.class, obj -> obj.containsKey( "x" ) ).exists() );
        assertEquals( 42, JsonMixed.of( "42" ).find( JsonNumber.class, JsonValue::isInteger ).intValue() );
    }

    @Test
    void testFind_Null() {
        assertFalse( JsonMixed.of( "null" ).find( JsonObject.class, obj -> obj.containsKey( "x" ) ).exists() );
    }

    @Test
    void testFind_Undefined() {
        JsonObject undefined = JsonMixed.of( "{}" ).getObject( "x" );
        assertFalse( undefined.find( JsonObject.class, obj -> obj.containsKey( "y" ) ).exists() );
        assertFalse( undefined.find( JsonValue.class, JsonValue::exists ).exists() );
    }
}

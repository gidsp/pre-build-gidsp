package org.fastable.gidsp.jsontree.validation;

import org.fastable.gidsp.jsontree.JsonInteger;
import org.fastable.gidsp.jsontree.JsonMap;
import org.fastable.gidsp.jsontree.JsonMixed;
import org.fastable.gidsp.jsontree.JsonObject;
import org.fastable.gidsp.jsontree.Validation;
import org.fastable.gidsp.jsontree.Validation.NodeType;
import org.fastable.gidsp.jsontree.Validation.Rule;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.fastable.gidsp.jsontree.Validation.YesNo.YES;
import static org.fastable.gidsp.jsontree.Assertions.assertValidationError;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Tests Validation of the @{@link Validation#maxProperties()} property.
 *
 * @author Jan Bernitt
 */
class JsonValidationMaxPropertiesTest {

    public interface JsonMaxPropertiesExampleA extends JsonObject {

        @Validation( maxProperties = 2, required = YES )
        default Map<String, Integer> config() {
            return getMap( "config", JsonInteger.class ).toMap( JsonInteger::integer );
        }
    }

    public interface JsonMaxPropertiesExampleB extends JsonObject {

        @Validation( maxProperties = 3 )
        default JsonMap<JsonInteger> points() {
            return getMap( "points", JsonInteger.class );
        }
    }

    @Test
    void testMaxProperties_OK() {
        assertDoesNotThrow( () -> JsonMixed.of( """
            {"config":{"a": 1, "b": 2}}""" ).validate( JsonMaxPropertiesExampleA.class ) );

        assertDoesNotThrow( () -> JsonMixed.of( """
            {"points":{"zero": 0, "x": 7, "y": 9}}""" ).validate( JsonMaxPropertiesExampleB.class ) );
    }

    @Test
    void testMaxProperties_Undefined() {
        assertValidationError( "{}", JsonMaxPropertiesExampleA.class, Rule.REQUIRED, "config" );

        assertDoesNotThrow( () -> JsonMixed.of( """
            {"points":{}}""" ).validate( JsonMaxPropertiesExampleB.class ) );
        assertDoesNotThrow( () -> JsonMixed.of( """
            {"points":null}""" ).validate( JsonMaxPropertiesExampleB.class ) );
        assertDoesNotThrow( () -> JsonMixed.of( """
            {}""" ).validate( JsonMaxPropertiesExampleB.class ) );
    }

    @Test
    void testMaxProperties_TooMany() {
        assertValidationError( """
                {"config":{"hey": 1, "ho": 2, "silver": 3}}""", JsonMaxPropertiesExampleA.class, Rule.MAX_PROPERTIES, 2,
            3 );
        assertValidationError( """
                {"points":{"x": 1, "y":  2, "z": 3, "w": 5}}""", JsonMaxPropertiesExampleB.class, Rule.MAX_PROPERTIES, 3,
            4 );
    }

    @Test
    void testMaxProperties_WrongType() {
        assertValidationError( """
                {"config":true}""", JsonMaxPropertiesExampleA.class, Rule.TYPE, Set.of( NodeType.OBJECT ),
            NodeType.BOOLEAN );
        assertValidationError( """
                {"points":true}""", JsonMaxPropertiesExampleB.class, Rule.TYPE, Set.of( NodeType.OBJECT ),
            NodeType.BOOLEAN );
    }
}

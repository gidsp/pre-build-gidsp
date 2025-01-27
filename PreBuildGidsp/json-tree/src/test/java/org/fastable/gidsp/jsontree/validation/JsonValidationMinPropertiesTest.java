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

import static org.fastable.gidsp.jsontree.Validation.YesNo.NO;
import static org.fastable.gidsp.jsontree.Assertions.assertValidationError;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Tests Validation of the @{@link Validation#minProperties()} property.
 *
 * @author Jan Bernitt
 */
class JsonValidationMinPropertiesTest {

    public interface JsonMinPropertiesExampleA extends JsonObject {

        @Validation( minProperties = 2 )
        default Map<String, Integer> config() {
            return getMap( "config", JsonInteger.class ).toMap( JsonInteger::integer );
        }
    }

    public interface JsonMinPropertiesExampleB extends JsonObject {

        @Validation( minProperties = 3, required = NO )
        default JsonMap<JsonInteger> points() {
            return getMap( "points", JsonInteger.class );
        }
    }

    @Test
    void testMinProperties_OK() {
        assertDoesNotThrow( () -> JsonMixed.of( """
            {"config":{"a": 1, "b": 2}}""" ).validate( JsonMinPropertiesExampleA.class ) );

        assertDoesNotThrow( () -> JsonMixed.of( """
            {"points":{"zero": 0, "x": 7, "y": 9}}""" ).validate( JsonMinPropertiesExampleB.class ) );
    }

    @Test
    void testMinProperties_Undefined() {
        assertValidationError( "{}", JsonMinPropertiesExampleA.class, Rule.REQUIRED, "config" );

        assertValidationError( """
            {"points":{}}""", JsonMinPropertiesExampleB.class, Rule.MIN_PROPERTIES, 3, 0 );
        assertDoesNotThrow( () -> JsonMixed.of( """
            {"points":null}""" ).validate( JsonMinPropertiesExampleB.class ) );
        assertDoesNotThrow( () -> JsonMixed.of( """
            {}""" ).validate( JsonMinPropertiesExampleB.class ) );
    }

    @Test
    void testMinProperties_TooFew() {
        assertValidationError( """
            {"config":{"hey": 1}}""", JsonMinPropertiesExampleA.class, Rule.MIN_PROPERTIES, 2, 1 );
        assertValidationError( """
            {"points":{}}""", JsonMinPropertiesExampleB.class, Rule.MIN_PROPERTIES, 3, 0 );
        assertValidationError( """
            {"points":{"x": 1, "y":  2}}""", JsonMinPropertiesExampleB.class, Rule.MIN_PROPERTIES, 3, 2 );
    }

    @Test
    void testMinProperties_WrongType() {
        assertValidationError( """
                {"config":true}""", JsonMinPropertiesExampleA.class, Rule.TYPE, Set.of( NodeType.OBJECT ),
            NodeType.BOOLEAN );
        assertValidationError( """
                {"points":true}""", JsonMinPropertiesExampleB.class, Rule.TYPE, Set.of( NodeType.OBJECT ),
            NodeType.BOOLEAN );
    }
}

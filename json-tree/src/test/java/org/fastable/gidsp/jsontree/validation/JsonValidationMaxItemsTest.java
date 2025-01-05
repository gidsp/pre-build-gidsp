package org.fastable.gidsp.jsontree.validation;

import org.fastable.gidsp.jsontree.JsonInteger;
import org.fastable.gidsp.jsontree.JsonList;
import org.fastable.gidsp.jsontree.JsonMixed;
import org.fastable.gidsp.jsontree.JsonObject;
import org.fastable.gidsp.jsontree.Validation;
import org.fastable.gidsp.jsontree.Validation.NodeType;
import org.fastable.gidsp.jsontree.Validation.Rule;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.fastable.gidsp.jsontree.Validation.YesNo.YES;
import static org.fastable.gidsp.jsontree.Assertions.assertValidationError;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Tests Validation of the @{@link Validation#maxItems()} property.
 *
 * @author Jan Bernitt
 */
class JsonValidationMaxItemsTest {

    public interface JsonMaxItemsExampleA extends JsonObject {

        @Validation( maxItems = 2, required = YES )
        default List<String> names() {
            return getArray( "names" ).stringValues();
        }
    }

    public interface JsonMaxItemsExampleB extends JsonObject {

        @Validation( maxItems = 3 )
        default JsonList<JsonInteger> points() {
            return getList( "points", JsonInteger.class );
        }
    }

    @Test
    void testMaxItems_OK() {
        assertDoesNotThrow( () -> JsonMixed.of( """
            {"names":["foo", "bar"]}""" ).validate( JsonMaxItemsExampleA.class ) );

        assertDoesNotThrow( () -> JsonMixed.of( """
            {"points":[1,2,3]}""" ).validate( JsonMaxItemsExampleB.class ) );
    }

    @Test
    void testMaxItems_Undefined() {
        assertValidationError( "{}", JsonMaxItemsExampleA.class, Rule.REQUIRED, "names" );

        assertDoesNotThrow( () -> JsonMixed.of( """
            {"points":[]}""" ).validate( JsonMaxItemsExampleB.class ) );
        assertDoesNotThrow( () -> JsonMixed.of( """
            {"points":null}""" ).validate( JsonMaxItemsExampleB.class ) );
        assertDoesNotThrow( () -> JsonMixed.of( """
            {}""" ).validate( JsonMaxItemsExampleB.class ) );
    }

    @Test
    void testMaxItems_TooMany() {
        assertValidationError( """
            {"names":["hey", "ho", "silver"]}""", JsonMaxItemsExampleA.class, Rule.MAX_ITEMS, 2, 3 );
        assertValidationError( """
            {"points":[1,2,3,4]}""", JsonMaxItemsExampleB.class, Rule.MAX_ITEMS, 3, 4 );
    }

    @Test
    void testMaxItems_WrongType() {
        assertValidationError( """
            {"names":true}""", JsonMaxItemsExampleA.class, Rule.TYPE, Set.of( NodeType.ARRAY ), NodeType.BOOLEAN );
        assertValidationError( """
            {"points":true}""", JsonMaxItemsExampleB.class, Rule.TYPE, Set.of( NodeType.ARRAY ), NodeType.BOOLEAN );
    }
}

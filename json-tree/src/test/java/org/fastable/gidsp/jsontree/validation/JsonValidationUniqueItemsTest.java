package org.fastable.gidsp.jsontree.validation;

import org.fastable.gidsp.jsontree.JsonArray;
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
 * Tests Validation of the @{@link Validation#uniqueItems()} property.
 *
 * @author Jan Bernitt
 */
class JsonValidationUniqueItemsTest {

    public interface JsonUniqueItemsExampleA extends JsonObject {

        @Validation( uniqueItems = YES, required = YES )
        default List<String> names() {
            return getArray( "names" ).stringValues();
        }
    }

    public interface JsonUniqueItemsExampleB extends JsonObject {

        @Validation( uniqueItems = YES )
        default JsonArray points() {
            return getArray( "points" );
        }
    }

    @Test
    void testUniqueItems_OK() {
        assertDoesNotThrow( () -> JsonMixed.of( """
            {"names":["foo", "bar"]}""" ).validate( JsonUniqueItemsExampleA.class ) );

        assertDoesNotThrow( () -> JsonMixed.of( """
            {"points":[1,2,3]}""" ).validate( JsonUniqueItemsExampleB.class ) );
    }

    @Test
    void testUniqueItems_Undefined() {
        assertValidationError( "{}", JsonUniqueItemsExampleA.class, Rule.REQUIRED, "names" );

        assertDoesNotThrow( () -> JsonMixed.of( """
            {}""" ).validate( JsonUniqueItemsExampleB.class ) );
        assertDoesNotThrow( () -> JsonMixed.of( """
            {"points":null}""" ).validate( JsonUniqueItemsExampleB.class ) );
        assertDoesNotThrow( () -> JsonMixed.of( """
            {"points":[]}""" ).validate( JsonUniqueItemsExampleB.class ) );
    }

    @Test
    void testUniqueItems_Duplicates() {
        assertValidationError( """
            {"names":["hey", "ho", "hey" ]}""", JsonUniqueItemsExampleA.class, Rule.UNIQUE_ITEMS, "\"hey\"", 0, 2 );
        assertValidationError( """
            {"points":[[],[1,2],[3],[ 1, 2 ]]}""", JsonUniqueItemsExampleB.class, Rule.UNIQUE_ITEMS, "[1,2]", 1, 3 );
    }

    @Test
    void testUniqueItems_WrongType() {
        assertValidationError( """
            {"names":true}""", JsonUniqueItemsExampleA.class, Rule.TYPE, Set.of( NodeType.ARRAY ), NodeType.BOOLEAN );
        assertValidationError( """
            {"points":true}""", JsonUniqueItemsExampleB.class, Rule.TYPE, Set.of( NodeType.ARRAY ), NodeType.BOOLEAN );
    }
}

package org.fastable.gidsp.jsontree.validation;

import org.fastable.gidsp.jsontree.JsonMixed;
import org.fastable.gidsp.jsontree.JsonObject;
import org.fastable.gidsp.jsontree.Validation;
import org.fastable.gidsp.jsontree.Validation.NodeType;
import org.fastable.gidsp.jsontree.Validation.Rule;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.fastable.gidsp.jsontree.Validation.YesNo.YES;
import static org.fastable.gidsp.jsontree.Assertions.assertValidationError;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Tests Validation of the @{@link Validation#maxLength()} property.
 *
 * @author Jan Bernitt
 */
class JsonValidationMaxLengthTest {

    public interface JsonMaxLengthExampleA extends JsonObject {

        @Validation( maxLength = 2, required = YES )
        default String name() {
            return getString( "name" ).string();
        }
    }

    public interface JsonMaxLengthExampleB extends JsonObject {

        @Validation( maxLength = 3 )
        default String abbr() {
            return getString( "abbr" ).string();
        }
    }

    @Test
    void testMaxLength_OK() {
        assertDoesNotThrow( () -> JsonMixed.of( """
            {"name":"yo"}""" ).validate( JsonMaxLengthExampleA.class ) );

        assertDoesNotThrow( () -> JsonMixed.of( """
            {"abbr":"WAT"}""" ).validate( JsonMaxLengthExampleB.class ) );
    }

    @Test
    void testMaxLength_Undefined() {
        assertValidationError( "{}", JsonMaxLengthExampleA.class, Rule.REQUIRED, "name" );

        assertDoesNotThrow( () -> JsonMixed.of( """
            {"abbr":null}""" ).validate( JsonMaxLengthExampleB.class ) );
        assertDoesNotThrow( () -> JsonMixed.of( """
            {}""" ).validate( JsonMaxLengthExampleB.class ) );
    }

    @Test
    void testMaxLength_TooShort() {
        assertValidationError( """
            {"name":"nop"}""", JsonMaxLengthExampleA.class, Rule.MAX_LENGTH, 2, 3 );
        assertValidationError( """
            {"abbr":"yeah"}""", JsonMaxLengthExampleB.class, Rule.MAX_LENGTH, 3, 4 );
    }

    @Test
    void testMaxLength_WrongType() {
        assertValidationError( """
            {"name":true}""", JsonMaxLengthExampleA.class, Rule.TYPE, Set.of( NodeType.STRING ), NodeType.BOOLEAN );
        assertValidationError( """
            {"abbr":true}""", JsonMaxLengthExampleB.class, Rule.TYPE, Set.of( NodeType.STRING ), NodeType.BOOLEAN );
    }
}

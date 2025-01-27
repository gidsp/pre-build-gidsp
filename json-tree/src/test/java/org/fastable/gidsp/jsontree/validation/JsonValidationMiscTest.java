package org.fastable.gidsp.jsontree.validation;

import org.fastable.gidsp.jsontree.JsonList;
import org.fastable.gidsp.jsontree.JsonMixed;
import org.fastable.gidsp.jsontree.JsonObject;
import org.fastable.gidsp.jsontree.Validation;
import org.junit.jupiter.api.Test;

import java.lang.annotation.RetentionPolicy;

import static org.fastable.gidsp.jsontree.Assertions.assertValidationError;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonValidationMiscTest {

    public interface JsonAnnotation extends JsonObject {

        RetentionPolicy getType();
    }

    public interface JsonContainer extends JsonObject {

        @Validation( maxLength = 5 )
        default String id() {
            return getString( "id" ).string();
        }

        default JsonList<JsonDetail> details() {
            return getList( "details", JsonDetail.class );
        }
    }

    public interface JsonDetail extends JsonObject {

        default JsonList<JsonContainer> errors() {
            return getList( "errors", JsonContainer.class );
        }
    }

    @Test
    void testValidation_RecursiveType() {
        assertTrue( JsonMixed.of( """
            {"type": "SOURCE"}""" ).isA( JsonAnnotation.class ) );
    }

    @Test
    void testValidation_RecursiveJsonStructure() {
        assertTrue( JsonMixed.of( """
            {"details": []}""" ).isA( JsonContainer.class ) );
        assertValidationError( """
                {"details": [{"errors": [{"id": "yesitworks"}]}]}""", JsonContainer.class, Validation.Rule.MAX_LENGTH, 5,
            10 );
    }

}

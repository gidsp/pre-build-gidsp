package org.fastable.gidsp.jsontree.validation;

import org.fastable.gidsp.jsontree.JsonObject;
import org.fastable.gidsp.jsontree.Validation;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.fastable.gidsp.jsontree.Validation.Rule.MAX_LENGTH;
import static org.fastable.gidsp.jsontree.Validation.Rule.MIN_LENGTH;
import static org.fastable.gidsp.jsontree.Validation.Rule.REQUIRED;
import static org.fastable.gidsp.jsontree.Validation.YesNo.YES;
import static org.fastable.gidsp.jsontree.Assertions.assertValidationError;

class JsonValidationMiscMetaAnnotationTest {

    @Retention( RUNTIME )
    @Validation( minLength = 11, maxLength = 11, required = YES )
    public @interface UID {}

    public interface JsonMetaExampleA extends JsonObject {

        @UID
        default String getUID() {
            return getString( "id" ).string();
        }
    }

    @Test
    void testMeta_MinLength() {
        assertValidationError( """
            {"id":  "hello"}""", JsonMetaExampleA.class, MIN_LENGTH, 11, 5 );
    }

    @Test
    void testMeta_MaxLength() {
        assertValidationError( """
            {"id":  "helloworld01"}""", JsonMetaExampleA.class, MAX_LENGTH, 11, 12 );
    }

    @Test
    void testMeta_Required() {
        assertValidationError( "{}", JsonMetaExampleA.class, REQUIRED, "id" );
    }
}

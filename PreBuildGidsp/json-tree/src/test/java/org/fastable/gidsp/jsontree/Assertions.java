package org.fastable.gidsp.jsontree;

import org.fastable.gidsp.jsontree.JsonAbstractObject;
import org.fastable.gidsp.jsontree.JsonMixed;
import org.fastable.gidsp.jsontree.JsonSchemaException;
import org.fastable.gidsp.jsontree.Validation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class Assertions {

    public static Validation.Error assertValidationError( String actualJson,
        Class<? extends JsonObject> schema,
        Validation.Rule expected, Object... args ) {
        return assertValidationError( JsonMixed.of( actualJson ), schema, expected, args );
    }

    public static Validation.Error assertValidationError( JsonObject actual,
        Class<? extends JsonObject> schema,
        Validation.Rule expected, Object... args ) {
        JsonSchemaException ex = assertThrowsExactly( JsonSchemaException.class, () -> actual.validate( schema ),
            "expected an error of type " + expected );
        List<Validation.Error> errors = ex.getInfo().errors();
        assertEquals( 1, errors.size(), "unexpected number of errors" );
        Validation.Error error = errors.get( 0 );
        assertEquals( expected, error.rule(), "unexpected error type" );
        assertEquals( List.of( args ), error.args(), "unexpected error arguments" );
        return error;
    }
}

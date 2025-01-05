package org.fastable.gidsp.jsontree.validation;

import org.fastable.gidsp.jsontree.JsonAbstractObject;
import org.fastable.gidsp.jsontree.JsonArray;
import org.fastable.gidsp.jsontree.JsonMixed;
import org.fastable.gidsp.jsontree.JsonObject;
import org.fastable.gidsp.jsontree.JsonPathException;
import org.fastable.gidsp.jsontree.JsonSchemaException;
import org.fastable.gidsp.jsontree.JsonSchemaException.Info;
import org.fastable.gidsp.jsontree.JsonTreeException;
import org.fastable.gidsp.jsontree.JsonValue;
import org.fastable.gidsp.jsontree.Validation;
import org.fastable.gidsp.jsontree.Validation.Error;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jan Bernitt
 * @since 0.11
 */
public final class JsonValidator {

    public static void validate( JsonValue value, Class<? extends JsonObject> schema ) {
        validate( value, schema, Set.of() );
    }

    public static void validate( JsonValue value, Class<? extends JsonObject> schema, Validation.Rule... rules ) {
        validate( value, schema, Set.of( rules ) );
    }

    public static void validate( JsonValue value, Class<? extends JsonObject> schema, Set<Validation.Rule> rules ) {
        if (!schema.isInterface())
            throw new IllegalArgumentException("Must be an interface bust was: "+schema);
        if ( !value.exists() )
            throw new JsonPathException( value.path(),
                String.format( "Value %s %s node does not exist", value.path(), schema.getSimpleName() ) );
        if ( !value.isObject() ) {
            throw new JsonTreeException(
                String.format( "Value %s %s node is not an object but a %s", value.path(), schema.getSimpleName(),
                    value.type() ) );
        }

        ObjectValidator validator = ObjectValidator.getInstance( schema );
        List<Error> errors = new ArrayList<>();
        for ( Map.Entry<String, Validation.Validator> e : validator.properties().entrySet() ) {
            JsonValue property = value.asObject().get( e.getKey() );
            e.getValue().validate( property.as( JsonMixed.class ), errors::add );
        }
        if ( !rules.isEmpty() )
            errors = errors.stream().filter( e -> rules.contains( e.rule() ) ).toList();
        if ( !errors.isEmpty() ) throw new JsonSchemaException( "%d errors".formatted( errors.size() ),
            new Info( value, schema, errors ) );
    }
}

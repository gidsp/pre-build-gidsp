package org.fastable.gidsp.jsontree;

import org.fastable.gidsp.jsontree.internal.Surly;

import java.io.Reader;
import java.nio.file.Path;

import static org.fastable.gidsp.jsontree.Validation.NodeType.ARRAY;
import static org.fastable.gidsp.jsontree.Validation.NodeType.BOOLEAN;
import static org.fastable.gidsp.jsontree.Validation.NodeType.INTEGER;
import static org.fastable.gidsp.jsontree.Validation.NodeType.NUMBER;
import static org.fastable.gidsp.jsontree.Validation.NodeType.OBJECT;
import static org.fastable.gidsp.jsontree.Validation.NodeType.STRING;

/**
 * Main API to wrap JSON raw strings as {@link JsonValue} nodes.
 * <p>
 * {@link JsonValue} is the bottom type or base type of possible JSON values. It represents a value of "unknown" node
 * type.
 * <p>
 * {@linkplain JsonMixed} is the union type of all possible core JSON types. It is a convenience starting point that can
 * be treated as any node type. It can also be used to represent a value that knowingly can be of different JSON node
 * types.
 *
 * @author Jan Bernitt
 * @see JsonValue
 * @since 0.8
 */
@Validation( type = { OBJECT, ARRAY, STRING, NUMBER, INTEGER, BOOLEAN } )
@Validation.Ignore
public interface JsonMixed extends JsonObject, JsonArray, JsonString, JsonNumber, JsonBoolean, JsonInteger {

    /**
     * Lift an actual {@link JsonNode} tree to a virtual {@link JsonValue}.
     *
     * @param node non null
     * @return the provided {@link JsonNode} as virtual {@link JsonMixed}
     */
    static JsonMixed of( JsonNode node ) {
        return new JsonVirtualTree( node.extract(), JsonTypedAccess.GLOBAL );
    }

    /**
     * View the provided JSON string as virtual lazy evaluated tree.
     *
     * @param json a standard conform JSON string
     * @return root of the virtual tree representing the given JSON input
     */
    static JsonMixed of( String json ) {
        return of( json, JsonTypedAccess.GLOBAL );
    }

    /**
     * View the provided JSON string as virtual lazy evaluated tree using the provided {@link JsonTypedAccessStore} for
     * mapping to Java method return type.
     *
     * @param json  a standard conform JSON string
     * @param store mapping used to map JSON values to the Java method return type of abstract methods, when
     *              {@code null} default mapping is used
     * @return root of the virtual tree representing the given JSON input
     */
    static JsonMixed of( String json, @Surly JsonTypedAccessStore store ) {
        return new JsonVirtualTree( json, store );
    }

    /**
     * Uses a more lenient parser to read the provided JSON input.
     * <p>
     * Beyond the standard this allows:
     * <ul>
     *     <li>single quoted strings (no escape)</li>
     * </ul>
     *
     * @param json a JSON input that is not standard conform
     * @return root of the virtual tree representing the given JSON input
     * @since 0.10
     */
    static JsonMixed ofNonStandard( String json ) {
        return of( JsonNode.ofNonStandard( json ) );
    }

    /**
     * @param file a JSON file in UTF-8 encoding
     * @return root of the virtual tree representing the given JSON input
     * @since 1.0
     */
    static JsonMixed of( Path file ) {
        return of(JsonNode.of( file ));
    }

    /**
     * @param json JSON input
     * @return root of the virtual tree representing the given JSON input
     * @since 1.0
     */
    static JsonMixed of( Reader json ) {
        return of(JsonNode.of( json, null ));
    }
}

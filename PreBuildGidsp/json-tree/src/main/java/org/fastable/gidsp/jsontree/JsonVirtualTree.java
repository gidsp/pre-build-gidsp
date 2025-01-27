/*
 * Copyright (c) 2004-2021, General Intergrate Date Service Platform
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.fastable.gidsp.jsontree;

import org.fastable.gidsp.jsontree.JsonTypedAccessStore.JsonGenericTypedAccessor;
import org.fastable.gidsp.jsontree.internal.Maybe;
import org.fastable.gidsp.jsontree.internal.Surly;

import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

/**
 * Implements the {@link JsonValue} read-only access abstraction for JSON responses.
 * <p>
 * The way this works is that internally when navigating the JSON object the {@link #path} is extended. The returned
 * value is always either a {@link JsonVirtualTree} or a {@link Proxy} calling all default methods on the declaring
 * interface and all implemented by {@link JsonVirtualTree} on a "wrapped" {@link JsonVirtualTree} instance.
 * <p>
 * When values are accessed the {@link #path} is extracted from the {@link #root} and eventually converted and checked
 * against expectations. Exceptions are eventually converted to provide a consistent behaviour.
 * <p>
 * It is crucial to understand that the complete JSON object model is purely a typed convenience layer expressing what
 * could or is expected to exist. Whether something actually exist is first evaluated when leaf values are accessed or
 * existence is explicitly checked using {@link #exists()}.
 * <p>
 * This also means specific {@link JsonObject}s are modelled by extending the interface and implementing {@code default}
 * methods. No other implementation {@code class} is ever needed apart from the {@link JsonVirtualTree}.
 *
 * @author Jan Bernitt
 */
final class JsonVirtualTree implements JsonMixed, Serializable {

    public static final JsonMixed NULL = new JsonVirtualTree( JsonNode.NULL, "$", new Access( JsonTypedAccess.GLOBAL, null) );

    private static final Map<Class<? extends JsonObject>, List<Property>> PROPERTIES = new ConcurrentHashMap<>();

    public static List<Property> properties(Class<? extends JsonObject> of) {
        return PROPERTIES.computeIfAbsent( of, JsonVirtualTree::captureProperties );
    }

    /**
     * {@link MethodHandle} cache for zero-args default methods as usually used by the properties declared in
     * {@link JsonObject} sub-types. Each subtype has its on map with the method name as key as that is already unique.
     */
    private static final ClassValue<Map<String, MethodHandle>> PROPERTY_MH_CACHE = new ClassValue<>() {
        @Override
        protected Map<String, MethodHandle> computeValue( Class declaringClass ) {
            return new ConcurrentHashMap<>();
        }
    };

    /**
     * {@link MethodHandle} cache used for any method that does not match conditions for {@link #PROPERTY_MH_CACHE}.
     * <p>
     * {@link MethodHandle}s are cached as a performance optimisation, in particular because during the MH resolve
     * exceptions might be thrown and caught internally which has shown to be costly compared to a cache lookup.
     */
    private static final Map<Method, MethodHandle> OTHER_MH_CACHE = new ConcurrentHashMap<>();

    /**
     * The access support is shared by all values that are derived from the same initial virtual tree.
     * Therefore, it makes sense to group them in a single object to reduce the fields needed for each node.
     */
    private record Access(@Surly JsonTypedAccessStore store, @Maybe ConcurrentMap<String, Object> cache) {}

    private final @Surly JsonNode root;
    private final @Surly String path;
    private final transient @Surly Access access;

    public JsonVirtualTree( @Maybe String json, @Surly JsonTypedAccessStore store ) {
        this( json == null || json.isEmpty() ? JsonNode.EMPTY_OBJECT : JsonNode.of( json ), "$", new Access(  store, null ));
    }

    public JsonVirtualTree( @Surly JsonNode root, @Surly JsonTypedAccessStore store ) {
        this( root, "$", new Access( store, null) );
    }

    private JsonVirtualTree( @Surly JsonNode root, @Surly String path, @Surly Access access ) {
        this.root = root;
        this.path = path;
        this.access = access;
    }

    @Surly @Override
    public String path() {
        return path;
    }

    @Surly @Override
    public JsonTypedAccessStore getAccessStore() {
        return access.store;
    }

    @Override
    public boolean isAccessCached() {
        return access.cache != null;
    }

    @Override
    public JsonVirtualTree withAccessCached() {
        return isAccessCached()
            ? this
            : new JsonVirtualTree( root, path, new Access( access.store, new ConcurrentHashMap<>() ) );
    }

    @Override
    public Class<? extends JsonValue> asType() {
        return JsonMixed.class;
    }

    private <T> T value( JsonNodeType expected, Function<JsonNode, T> get, T orElse ) {
        try {
            JsonNode node = root.getOrNull( path );
            if (node == null) return orElse;
            JsonNodeType actualType = node.getType();
            if ( actualType == JsonNodeType.NULL ) {
                return null;
            }
            if ( actualType != expected ) {
                throw new JsonTreeException(
                    String.format( "Path `%s` does not contain an %s but a(n) %s: %s",
                        path, expected, actualType, node ) );
            }
            return get.apply( node );
        } catch ( JsonPathException ex ) {
            return orElse;
        }
    }

    private JsonNode value() {
        return root.get( path );
    }

    @Override
    public <T extends JsonValue> T get( int index, Class<T> as ) {
        return asType( as, new JsonVirtualTree( root, path + "[" + index + "]", access ) );
    }

    @Override
    public <T extends JsonValue> T get( String name, Class<T> as ) {
        if ( name.isEmpty() ) return as( as );
        boolean isQualified = name.startsWith( "{" ) || name.startsWith( "." ) || name.startsWith( "[" );
        String canonicalPath = isQualified ? path + name : path + "." + name;
        return asType( as, new JsonVirtualTree( root, canonicalPath, access) );
    }

    @Override
    public <T extends JsonValue> T as( Class<T> as ) {
        return asType( as, this );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends JsonValue> T as( Class<T> as, BiConsumer<Method, Object[]> onCall ) {
        return (T) Proxy.newProxyInstance(
            Thread.currentThread().getContextClassLoader(), new Class[] { as },
            ( proxy, method, args ) -> {
                onCall.accept( method, args );
                return onInvoke( proxy, as, this, method, args, true );
            } );
    }

    @SuppressWarnings( "unchecked" )
    private <T extends JsonValue> T asType( Class<T> as, JsonVirtualTree target ) {
        return isJsonMixedSubType( as ) ? createProxy( as, target ) : (T) target;
    }

    @Override
    public <A, B> B mapNonNull( A from, Function<A, B> to ) {
        if ( from == null ) {
            root.get( path ); // cause throw in case node does not exist
        }
        return to.apply( from );
    }

    @Override
    public JsonNode node() {
        return value();
    }

    @Override
    public boolean isEmpty() {
        return value().isEmpty();
    }

    @Override
    public List<String> stringValues() {
        return arrayList( String.class );
    }

    @Override
    public List<Number> numberValues() {
        return arrayList( Number.class );
    }

    @Override
    public List<Boolean> boolValues() {
        return arrayList( Boolean.class );
    }

    @SuppressWarnings( "unchecked" )
    private <T> List<T> arrayList( Class<T> elementType ) {
        return value( JsonNodeType.ARRAY, node -> {
            List<T> res = new ArrayList<>();
            for ( JsonNode e : node.elements() ) {
                Object value = e.value();
                if ( !elementType.isInstance( value ) ) {
                    throw new JsonTreeException(
                        "Array element is not a " + elementType.getName() + ": " + e.getDeclaration() );
                }
                res.add( (T) value );
            }
            return res;
        }, List.of() );
    }

    @Override
    public int size() {
        return value().size();
    }

    @Override
    public Boolean bool() {
        return (Boolean) value( JsonNodeType.BOOLEAN, JsonNode::value, null );
    }

    @Override
    public Number number() {
        return (Number) value( JsonNodeType.NUMBER, JsonNode::value, null );
    }

    @Override
    public String string() {
        return (String) value( JsonNodeType.STRING, JsonNode::value, null );
    }

    @Override
    public boolean exists() {
        try {
            return root.getOrNull( path ) != null;
        } catch ( JsonPathException ex ) {
            return false;
        }
    }

    @Override
    public boolean equals( Object obj ) {
        return obj instanceof JsonVirtualTree response
            && path.equals( response.path )
            && root.equals( response.root );
    }

    @Override
    public int hashCode() {
        return Objects.hash( root, path );
    }

    @Override
    public String toString() {
        try {
            return root.get( path ).getDeclaration();
        } catch ( JsonPathException | JsonFormatException ex ) {
            return ex.getMessage();
        }
    }

    @SuppressWarnings( "unchecked" )
    private <E extends JsonValue> E createProxy( Class<E> as, JsonVirtualTree target ) {
        return (E) Proxy.newProxyInstance(
            Thread.currentThread().getContextClassLoader(), new Class[] { as },
            ( proxy, method, args ) -> onInvoke( proxy, as, target, method, args, false ) );
    }

    /**
     * @param proxy instance of the proxy the method was invoked upon
     * @param as the type the proxy represents
     * @param target the underlying tree that in the end holds the node against which a call potentially is resolved
     * @param method the method of the proxy that was called
     * @param args the arguments for the method
     * @return the result of the method call
     * @param <E> type of the proxy
     */
    private <E extends JsonValue> Object onInvoke( Object proxy, Class<E> as, JsonVirtualTree target, Method method,
        Object[] args, boolean alwaysCallDefault )
        throws Throwable {
        // are we dealing with a default method in the extending class?
        Class<?> declaringClass = method.getDeclaringClass();
        if ( declaringClass == JsonValue.class && "asType".equals( method.getName() )
            && method.getParameterCount() == 0 ) {
            return as;
        }
        boolean isDefault = method.isDefault();
        if ( isJsonMixedSubType( declaringClass ) || isDefault && alwaysCallDefault ) {
            if ( isDefault ) {
                // call the default method of the proxied type itself
                return callDefaultMethod( proxy, method, args );
            }
            // abstract extending interface method?
            return callAbstractMethod( target, method, args );
        }
        // call the same method on the wrapped object (assuming it has it)
        return callCoreApiMethod( target, method, args );
    }

    /**
     * Any default methods implemented by an extension of the {@link JsonValue} class tree is run by calling the default
     * as defined in the interface. This is sadly not as straight forward as it might sound.
     */
    private static Object callDefaultMethod( Object proxy, Method method, Object[] args )
        throws Throwable {
        if (method.getParameterCount() == 0)
            return PROPERTY_MH_CACHE.get( method.getDeclaringClass() )
                .computeIfAbsent( method.getName(), name -> getDefaultMethodHandle( method ) )
                .bindTo( proxy ).invoke();
        return OTHER_MH_CACHE.computeIfAbsent( method, JsonVirtualTree::getDefaultMethodHandle )
            .bindTo( proxy ).invokeWithArguments( args );
    }

    /**
     * All methods by the core API of the general JSON tree represented as {@link JsonValue}s (and the general
     * subclasses) are implemented by the {@link JsonVirtualTree} wrapper, so they can be called directly.
     */
    private static Object callCoreApiMethod( JsonVirtualTree target, Method method, Object[] args )
        throws Throwable {
        if (args == null || args.length == 0)
            return OTHER_MH_CACHE.computeIfAbsent( method, JsonVirtualTree::getCoreApiMethodHandle )
                .bindTo( target ).invoke();
        if (method.isDefault())
            return OTHER_MH_CACHE.computeIfAbsent( method, JsonVirtualTree::getDefaultMethodHandle )
                .bindTo( target ).invokeWithArguments( args );
        return OTHER_MH_CACHE.computeIfAbsent(method, JsonVirtualTree::getCoreApiMethodHandle )
            .bindTo( target ).invokeWithArguments( args );
    }

    private static MethodHandle getCoreApiMethodHandle( Method m ) {
        try {
            return MethodHandles.lookup().unreflect( m );
        } catch ( IllegalAccessException e ) {
            throw new RuntimeException( e );
        }
    }

    private static MethodHandle getDefaultMethodHandle( Method method ) {
        try {
            Class<?> declaringClass = method.getDeclaringClass();
            return MethodHandles.lookup()
                .findSpecial( declaringClass, method.getName(),
                    MethodType.methodType( method.getReturnType(), method.getParameterTypes() ),
                    declaringClass );
        } catch ( Exception ex ) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Abstract interface methods are "implemented" by deriving an {@link JsonGenericTypedAccessor} from the method's
     * return type and have the accessor extract the value by using the underlying {@link JsonValue} API.
     */
    private Object callAbstractMethod( JsonVirtualTree target, Method method, Object[] args ) {
        JsonObject obj = target.asObject();
        Class<?> resType = method.getReturnType();
        String name = stripGetterPrefix( method );
        boolean hasDefault = method.getParameterCount() == 1 && method.getParameterTypes()[0] == resType;
        if ( obj.get( name ).isUndefined() && hasDefault ) {
            return args[0];
        }
        if ( access.cache != null && isCacheable( resType ) ) {
            String keyId = target.path + "." + name + ":" + toSignature( method.getGenericReturnType() );
            return access.cache.computeIfAbsent( keyId, key -> access( method, obj, name ) );
        }
        return access( method, obj, name );
    }

    private Object access( Method method, JsonObject obj, String name ) {
        Type genericType = method.getGenericReturnType();
        JsonGenericTypedAccessor<?> accessor = access.store.accessor( method.getReturnType() );
        if ( accessor != null )
            return accessor.access( obj, name, genericType, access.store );
        throw new UnsupportedOperationException( "No accessor registered for type: " + genericType );
    }

    /**
     * This is twofold: Concepts like {@link Stream} and {@link Iterator} should not be cached to work correctly.
     * <p>
     * For all other types this is about reaching a balance between memory usage and CPU usage. Simple objects are
     * recomputed whereas complex objects are not.
     */
    private boolean isCacheable( Class<?> resType ) {
        return resType.isInterface()
            && !Stream.class.isAssignableFrom( resType )
            && !Iterator.class.isAssignableFrom( resType )
            && !JsonPrimitive.class.isAssignableFrom( resType );
    }

    /**
     * Logically we check for JsonMixed but to be safe any method implemented by {@linkplain JsonVirtualTree} should be
     * considered as "core" and be handled directly
     */
    private static boolean isJsonMixedSubType( Class<?> declaringClass ) {
        return !declaringClass.isAssignableFrom( JsonVirtualTree.class );
    }

    private static String stripGetterPrefix( Method method ) {
        String name = method.getName();
        if ( name.startsWith( "is" ) && name.length() > 2 && isUpperCase( name.charAt( 2 ) ) ) {
            return toLowerCase( name.charAt( 2 ) ) + name.substring( 3 );
        }
        if ( name.startsWith( "get" ) && name.length() > 3 && isUpperCase( name.charAt( 3 ) ) ) {
            return toLowerCase( name.charAt( 3 ) ) + name.substring( 4 );
        }
        return name;
    }

    private String toSignature( Type type ) {
        if ( type instanceof Class<?> ) {
            return ((Class<?>) type).getCanonicalName();
        }
        if ( type instanceof ParameterizedType ) {
            StringBuilder str = new StringBuilder();
            toSignature( type, str );
            return str.toString();
        }
        return "?";
    }

    private void toSignature( Type type, StringBuilder str ) {
        if ( type instanceof Class<?> ) {
            str.append( ((Class<?>) type).getCanonicalName() );
            return;
        }
        if ( type instanceof ParameterizedType pt ) {
            toSignature( pt.getRawType(), str );
            str.append( '<' );
            for ( Type ata : pt.getActualTypeArguments() ) {
                str.append( toSignature( ata ) );
            }
            str.append( '>' );
        } else {
            str.append( '?' );
        }
    }

    private static List<Property> captureProperties(Class<? extends JsonObject> of) {
        List<Property> res = new ArrayList<>();
        propertyMethods(of).forEach( m -> {
            @SuppressWarnings( "unchecked" )
            Class<? extends JsonObject> in = (Class<? extends JsonObject>) m.getDeclaringClass();
            JsonObject obj = JsonMixed.of( "{}" ).as( of, (method, args) -> {
                if ( isJsonObjectGetAs( method ) ) {
                    String name = (String) args[0];
                    @SuppressWarnings( "unchecked" )
                    Class<? extends JsonValue> type = (Class<? extends JsonValue>) args[1];
                    res.add( new Property( in, name, type, m.getName(), m.getAnnotatedReturnType(), m ) );
                }
            });
            invokePropertyMethod( obj, m ); // may add zero, one or more properties via the callback
        } );
        return List.copyOf( res );
    }

    private static boolean isJsonObjectGetAs( Method method ) {
        return "get".equals( method.getName() )
            && method.getParameterCount() == 2
            && method.getDeclaringClass() == JsonObject.class;
    }

    private static void invokePropertyMethod(JsonObject obj, Method property) {
        try {
            MethodHandles.lookup().unreflect( property ).invokeWithArguments( obj );
        } catch ( Throwable e ) {
            // ignore
        }
    }

    private static Stream<Method> propertyMethods( Class<?> of ) {
        return Stream.of( of.getMethods() )
            .filter( JsonVirtualTree::isJsonObjectSubTypeProperty );
    }

    /**
     * @return Only true for methods declared in interfaces extending {@link JsonObject}. All core API methods are
     * excluded.
     */
    private static boolean isJsonObjectSubTypeProperty(Method m) {
        return m.isDefault()
            && m.getParameterCount() == 0
            && isJsonMixedSubType( m.getDeclaringClass() )
            && m.getReturnType() != void.class;
    }
}

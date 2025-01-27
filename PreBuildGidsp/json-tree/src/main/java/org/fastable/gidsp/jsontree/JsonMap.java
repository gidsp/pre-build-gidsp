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

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.fastable.gidsp.jsontree.Validation.NodeType.OBJECT;

/**
 * {@link JsonMap}s are a special form of a {@link JsonObject} where all properties have a common uniform value type.
 *
 * @param <E> type of the uniform map values
 * @author Jan Bernitt
 */
@Validation( type = OBJECT )
@Validation.Ignore
public interface JsonMap<E extends JsonValue> extends JsonAbstractObject<E> {

    /**
     * @param toValue from JSON to Java type
     * @return a Java {@link Map} of this {@link JsonMap}
     * @throws JsonTreeException in case this node does exist but is not an object node
     * @since 0.11
     */
    default <T> Map<String, T> toMap( Function<E, T> toValue ) {
        return entries().collect( Collectors.toMap( Map.Entry::getKey, e -> toValue.apply( e.getValue() ) ) );
    }

    /**
     * Maps this map's entry values to a lazy transformed map view where each entry value of the original map is
     * transformed by the given function when accessed.
     * <p>
     * This means the returned map always has same size as the original map.
     *
     * @param projection transformer function
     * @param <V>        type of the transformer output, entries of the map view
     * @return a lazily transformed map view of this map
     */
    default <V extends JsonValue> JsonMap<V> project( Function<E, V> projection ) {
        final class JsonMapProjection extends CollectionView<JsonMap<E>> implements JsonMap<V> {

            private JsonMapProjection( JsonMap<E> viewed ) {
                super( viewed );
            }

            @Override
            public V get( String key ) {
                return projection.apply( viewed.get( key ) );
            }

            @Override
            public Class<? extends JsonValue> asType() {
                return JsonMap.class;
            }
        }
        return new JsonMapProjection( this );
    }
}

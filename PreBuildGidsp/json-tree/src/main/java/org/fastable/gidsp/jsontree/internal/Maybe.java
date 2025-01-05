package org.fastable.gidsp.jsontree.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Something that could be null.
 *
 * @see Surly
 */
@Target( { ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD } )
public @interface Maybe {
}

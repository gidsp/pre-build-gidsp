package org.fastable.gidsp.jsontree;

import static org.fastable.gidsp.jsontree.Validation.NodeType.INTEGER;

/**
 * For numbers that always should be integers.
 * <p>
 * The main reason this exists is to benefit from more exact type validation.
 *
 * @author Jan Bernitt
 * @since 0.11
 */
@Validation( type = INTEGER )
@Validation.Ignore
public interface JsonInteger extends JsonNumber {
}

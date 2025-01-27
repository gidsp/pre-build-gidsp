package org.fastable.gidsp.antlr.operator;

/*
 * Copyright (c) 2004-2020, General Intergrate Date Service Platform
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

import org.fastable.gidsp.antlr.AntlrExprItem;
import org.fastable.gidsp.antlr.AntlrExpressionVisitor;

import java.util.ArrayList;
import java.util.List;

import static org.fastable.gidsp.parser.expression.antlr.ExpressionParser.ExprContext;

/**
 * A function that computes the result from expression arguments.
 * <p>
 * This abstract class evaluates the expression arguments and makes the
 * resulting values available to the subclass as a List.
 *
 * @author Jim Grace
 */
public abstract class AntlrComputeFunction
    implements AntlrExprItem
{
    @Override
    public final Object evaluate( ExprContext ctx, AntlrExpressionVisitor visitor )
    {
        List<Object> values = new ArrayList<>();

        for ( ExprContext expr : ctx.expr() )
        {
            Object value = visitor.visit( expr );

            if ( invalidArg( value ) )
            {
                return value;
            }

            values.add( value );
        }

        return compute( values );
    }

    /**
     * The default for a subclass is that if any of the arguments are null or
     * Double.NaN, then return null or Double.NaN, respectively.
     */
    protected boolean invalidArg( Object value )
    {
        return value == null || ( value instanceof Double && Double.isNaN( (Double) value ) );
    }

    /**
     * Computes the result from non-null values.
     *
     * @param values the values to use.
     * @return the computed value.
     */
    public abstract Object compute( List<Object> values );
}

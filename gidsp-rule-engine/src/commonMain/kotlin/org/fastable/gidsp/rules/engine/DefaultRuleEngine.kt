package org.fastable.gidsp.rules.engine

import org.hisp.dhis.lib.expression.Expression
import org.hisp.dhis.lib.expression.ExpressionMode
import org.hisp.dhis.lib.expression.spi.IllegalExpressionException
import org.hisp.dhis.lib.expression.spi.ParseException
import org.hisp.dhis.lib.expression.spi.ValueType
import org.fastable.gidsp.rules.api.DataItem
import org.fastable.gidsp.rules.api.RuleEngine
import org.fastable.gidsp.rules.api.RuleEngineContext
import org.fastable.gidsp.rules.models.*

internal class DefaultRuleEngine : RuleEngine {
    override fun evaluate(
        target: RuleEvent,
        ruleEnrollment: RuleEnrollment?,
        ruleEvents: List<RuleEvent>,
        executionContext: RuleEngineContext,
    ): List<RuleEffect> {
        val valueMap =
            RuleVariableValueMapBuilder().build(
                executionContext.constantsValues,
                executionContext.ruleVariables,
                ruleEvents.toSet(),
                ruleEnrollment,
                target,
            )
        return RuleConditionEvaluator().getRuleEffects(
            TrackerObjectType.EVENT,
            target.event,
            valueMap,
            executionContext.supplementaryData,
            executionContext.rules,
        )
    }

    override fun evaluate(
        target: RuleEnrollment,
        ruleEvents: List<RuleEvent>,
        executionContext: RuleEngineContext,
    ): List<RuleEffect> {
        val valueMap =
            RuleVariableValueMapBuilder().build(
                executionContext.constantsValues,
                executionContext.ruleVariables,
                ruleEvents.toSet(),
                target,
            )
        return RuleConditionEvaluator().getRuleEffects(
            TrackerObjectType.ENROLLMENT,
            target.enrollment,
            valueMap,
            executionContext.supplementaryData,
            executionContext.rules,
        )
    }

    override fun evaluateAll(
        enrollmentTarget: RuleEnrollment?,
        eventsTarget: List<RuleEvent>,
        executionContext: RuleEngineContext,
    ): List<RuleEffects> {
        val valueMap =
            RuleVariableValueMapBuilder()
                .multipleBuild(executionContext.constantsValues, executionContext.ruleVariables, eventsTarget.toSet(), enrollmentTarget)
        return RuleEngineMultipleExecution().execute(
            executionContext.rules,
            valueMap,
            executionContext.supplementaryData,
        )
    }

    override fun validate(
        expression: String,
        dataItemStore: Map<String, DataItem>,
    ): RuleValidationResult {
        // Rule condition expression should be evaluated against Boolean
        return getExpressionDescription(expression, ExpressionMode.RULE_ENGINE_CONDITION, dataItemStore)
    }

    override fun validateDataFieldExpression(
        expression: String,
        dataItemStore: Map<String, DataItem>,
    ): RuleValidationResult {
        // Rule action data field should be evaluated against all i.e Boolean, String, Date and Numerical value
        return getExpressionDescription(expression, ExpressionMode.RULE_ENGINE_ACTION, dataItemStore)
    }

    private fun getExpressionDescription(
        expression: String,
        mode: ExpressionMode,
        dataItemStore: Map<String, DataItem>,
    ): RuleValidationResult =
        try {
            val validationMap: Map<String, ValueType> = dataItemStore.mapValues { e -> e.value.valueType.toValueType() }
            Expression(expression, mode, false).validate(validationMap)
            val displayNames: Map<String, String> = dataItemStore.mapValues { e -> e.value.displayName }
            val description = Expression(expression, mode, false).describe(displayNames)
            RuleValidationResult(valid = true, description = description)
        } catch (ex: IllegalExpressionException) {
            RuleValidationResult(
                valid = false,
                exception = RuleEngineValidationException(ex),
                errorMessage = ex.message,
            )
        } catch (ex: ParseException) {
            RuleValidationResult(
                valid = false,
                exception = RuleEngineValidationException(ex),
                errorMessage = ex.message,
            )
        }
}

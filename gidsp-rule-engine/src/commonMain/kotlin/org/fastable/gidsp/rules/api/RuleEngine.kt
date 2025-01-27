package org.fastable.gidsp.rules.api

import org.fastable.gidsp.rules.engine.DefaultRuleEngine
import org.fastable.gidsp.rules.models.*
import kotlin.jvm.JvmStatic

interface RuleEngine {
    fun validate(
        expression: String,
        dataItemStore: Map<String, DataItem>,
    ): RuleValidationResult

    fun validateDataFieldExpression(
        expression: String,
        dataItemStore: Map<String, DataItem>,
    ): RuleValidationResult

    fun evaluateAll(
        enrollmentTarget: RuleEnrollment?,
        eventsTarget: List<RuleEvent>,
        executionContext: RuleEngineContext,
    ): List<RuleEffects>

    fun evaluate(
        target: RuleEnrollment,
        ruleEvents: List<RuleEvent>,
        executionContext: RuleEngineContext,
    ): List<RuleEffect>

    fun evaluate(
        target: RuleEvent,
        ruleEnrollment: RuleEnrollment?,
        ruleEvents: List<RuleEvent>,
        executionContext: RuleEngineContext,
    ): List<RuleEffect>

    companion object {
        @JvmStatic
        fun getInstance(): RuleEngine = DefaultRuleEngine()
    }
}

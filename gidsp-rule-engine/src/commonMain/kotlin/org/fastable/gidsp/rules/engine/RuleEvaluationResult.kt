package org.fastable.gidsp.rules.engine

import org.fastable.gidsp.rules.models.Rule
import org.fastable.gidsp.rules.models.RuleAction
import org.fastable.gidsp.rules.models.RuleEffect

internal data class RuleEvaluationResult(
    val rule: Rule,
    val ruleEffects: List<RuleEffect>,
    val evaluatedAs: Boolean,
    val error: Boolean,
) {
    companion object {
        fun evaluatedResult(
            rule: Rule,
            ruleEffects: List<RuleEffect>,
        ): RuleEvaluationResult = RuleEvaluationResult(rule, ruleEffects, true, false)

        fun notEvaluatedResult(rule: Rule): RuleEvaluationResult = RuleEvaluationResult(rule, emptyList(), false, false)

        fun errorRule(
            rule: Rule,
            errorMessage: String,
        ): RuleEvaluationResult {
            val effects =
                listOf(
                    RuleEffect(
                        rule.uid,
                        RuleAction(errorMessage, "ERROR"),
                        errorMessage,
                    ),
                )
            return RuleEvaluationResult(rule, effects, false, true)
        }
    }
}

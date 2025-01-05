package org.fastable.gidsp.rules.api

import org.fastable.gidsp.rules.models.Rule
import org.fastable.gidsp.rules.models.RuleVariable

data class RuleEngineContext(
    val rules: List<Rule>,
    val ruleVariables: List<RuleVariable> = emptyList(),
    val supplementaryData: Map<String, List<String>> = emptyMap(),
    val constantsValues: Map<String, String> = emptyMap(),
)

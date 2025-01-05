package org.fastable.gidsp.rules.models

data class RuleEffect(
    val ruleId: String,
    val ruleAction: RuleAction,
    val data: String? = "",
)

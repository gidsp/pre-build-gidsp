package org.fastable.gidsp.rules

@JsExport
data class RuleEffectJs(
    val ruleId: String,
    val ruleAction: RuleActionJs,
    val data: String? = ""
)
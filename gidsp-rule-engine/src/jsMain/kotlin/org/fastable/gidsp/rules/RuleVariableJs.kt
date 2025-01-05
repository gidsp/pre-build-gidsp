package org.fastable.gidsp.rules

import org.fastable.gidsp.rules.models.Option
import org.fastable.gidsp.rules.models.RuleValueType

@JsExport
data class RuleVariableJs(
    val type: RuleVariableType,
    val name: String,
    val useCodeForOptionSet: Boolean,
    val options: Array<Option>,
    val field: String,
    val fieldType: RuleValueType,
    val programStage: String?
)

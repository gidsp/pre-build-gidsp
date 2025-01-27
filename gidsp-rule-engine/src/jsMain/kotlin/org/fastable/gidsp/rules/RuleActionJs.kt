package org.fastable.gidsp.rules

import js.collections.JsMap

@JsExport
data class RuleActionJs(
    val data: String?,
    val type: String,
    val values: JsMap<String, String> = JsMap()
)
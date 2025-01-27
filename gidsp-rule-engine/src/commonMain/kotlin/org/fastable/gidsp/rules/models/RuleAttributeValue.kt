package org.fastable.gidsp.rules.models

import kotlin.js.JsExport

@JsExport
data class RuleAttributeValue(
    val trackedEntityAttribute: String,
    val value: String,
)

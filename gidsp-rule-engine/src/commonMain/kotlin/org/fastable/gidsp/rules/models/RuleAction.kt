package org.fastable.gidsp.rules.models

data class RuleAction(
    val data: String?,
    val type: String,
    val values: Map<String, String> = emptyMap(),
) {
    fun content(): String? = values["content"]

    fun field(): String? = values["field"]

    fun attributeType(): String? = values["attributeType"]
}

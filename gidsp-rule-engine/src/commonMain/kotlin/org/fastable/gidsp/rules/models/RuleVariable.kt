package org.fastable.gidsp.rules.models

import org.fastable.gidsp.rules.engine.RuleVariableValue

interface RuleVariable {
    val name: String
    val useCodeForOptionSet: Boolean
    val options: List<Option>
    val field: String
    val fieldType: RuleValueType

    fun createValues(
        ruleEvent: RuleEvent?,
        allEventValues: Map<String, List<RuleDataValueHistory>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>,
    ): RuleVariableValue

    fun getOptionName(value: String): String {
        // if no option found then existing value in the context will be used
        return options
            .filter { (_, code): Option -> value == code }
            .map(Option::name)
            .getOrElse(0) { _ -> value }
    }
}

package org.fastable.gidsp.rules.models

import org.fastable.gidsp.rules.engine.RuleVariableValue
import org.fastable.gidsp.rules.utils.getLastUpdateDate

class RuleVariableNewestStageEvent(
    override val name: String,
    override val useCodeForOptionSet: Boolean,
    override val options: List<Option>,
    override val field: String,
    override val fieldType: RuleValueType,
    val programStage: String,
) : RuleVariable {
    override fun createValues(
        ruleEvent: RuleEvent?,
        allEventValues: Map<String, List<RuleDataValueHistory>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>,
    ): RuleVariableValue {
        val stageRuleDataValues = allEventValues[field]?.filter { it.programStage == programStage }.orEmpty()

        return if (stageRuleDataValues.isEmpty()) {
            RuleVariableValue(fieldType)
        } else {
            val value = stageRuleDataValues[0]
            val optionValue = if (useCodeForOptionSet) value.value else getOptionName(value.value)
            RuleVariableValue(
                fieldType,
                optionValue,
                stageRuleDataValues.map { it.value },
                getLastUpdateDate(stageRuleDataValues),
            )
        }
    }
}

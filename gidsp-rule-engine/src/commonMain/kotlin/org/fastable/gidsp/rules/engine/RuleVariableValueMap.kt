package org.fastable.gidsp.rules.engine

import org.fastable.gidsp.rules.models.RuleEnrollment
import org.fastable.gidsp.rules.models.RuleEvent

internal data class RuleVariableValueMap(
    val enrollmentMap: Map<RuleEnrollment, Map<String, RuleVariableValue>>,
    val eventMap: Map<RuleEvent, Map<String, RuleVariableValue>>,
)

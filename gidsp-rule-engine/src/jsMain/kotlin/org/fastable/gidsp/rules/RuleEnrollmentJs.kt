package org.fastable.gidsp.rules

import org.fastable.gidsp.rules.models.RuleAttributeValue
import org.fastable.gidsp.rules.models.RuleEnrollmentStatus

@JsExport
data class RuleEnrollmentJs(
    val enrollment: String,
    val programName: String,
    val incidentDate: kotlinx.datetime.internal.JSJoda.LocalDate,
    val enrollmentDate: kotlinx.datetime.internal.JSJoda.LocalDate,
    val status: RuleEnrollmentStatus,
    val organisationUnit: String,
    val organisationUnitCode: String?,
    val attributeValues: Array<RuleAttributeValue>
)

package org.fastable.gidsp.rules

import org.fastable.gidsp.rules.models.TrackerObjectType

@JsExport
data class RuleEffectsJs(
    val trackerObjectType: TrackerObjectType,
    val trackerObjectUid: String,
    val ruleEffects: Array<RuleEffectJs>
)

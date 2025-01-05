package org.fastable.gidsp.rules.models

class RuleEngineValidationException(
    cause: IllegalArgumentException,
) : IllegalArgumentException(cause.message) 

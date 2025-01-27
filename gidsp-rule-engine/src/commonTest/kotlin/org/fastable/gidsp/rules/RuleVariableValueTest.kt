package org.fastable.gidsp.rules

import org.fastable.gidsp.rules.engine.RuleVariableValue
import org.fastable.gidsp.rules.models.RuleValueType
import kotlin.test.Test
import kotlin.test.assertEquals

class RuleVariableValueTest {
    @Test
    fun textValuesMostBeWrapped() {
        val variableValue =
            RuleVariableValue(
                RuleValueType.TEXT,
                "test_value",
                listOf(
                    "test_value_candidate_one",
                    "test_value_candidate_two",
                ),
                "2023-12-14",
            )
        assertEquals("test_value", variableValue.value)
        assertEquals(RuleValueType.TEXT, variableValue.type)
        assertEquals(2, variableValue.candidates.size)
        assertEquals("test_value_candidate_one", variableValue.candidates.get(0))
        assertEquals("test_value_candidate_two", variableValue.candidates.get(1))
    }

    @Test
    fun wrappedTextValuesMustNotBeDoubleWrapped() {
        val variableValue =
            RuleVariableValue(
                RuleValueType.TEXT,
                "test_value",
                listOf(
                    "test_value_candidate_one",
                    "test_value_candidate_two",
                ),
                "2023-12-14",
            )
        assertEquals("test_value", variableValue.value)
        assertEquals(RuleValueType.TEXT, variableValue.type)
        assertEquals(2, variableValue.candidates.size)
        assertEquals("test_value_candidate_one", variableValue.candidates.get(0))
        assertEquals("test_value_candidate_two", variableValue.candidates.get(1))
    }

    @Test
    fun numericValuesMostNotBeWrapped() {
        val variableValue =
            RuleVariableValue(
                RuleValueType.NUMERIC,
                "1",
                listOf("2", "3"),
                "2023-12-14",
            )
        assertEquals("1", variableValue.value)
        assertEquals(RuleValueType.NUMERIC, variableValue.type)
        assertEquals(2, variableValue.candidates.size)
        assertEquals("2", variableValue.candidates.get(0))
        assertEquals("3", variableValue.candidates.get(1))
    }

    @Test
    fun booleanValuesMostNotBeWrapped() {
        val variableValue =
            RuleVariableValue(
                RuleValueType.BOOLEAN,
                "true",
                listOf("false", "false"),
                "2023-12-14",
            )
        assertEquals("true", variableValue.value)
        assertEquals(RuleValueType.BOOLEAN, variableValue.type)
        assertEquals(2, variableValue.candidates.size)
        assertEquals("false", variableValue.candidates.get(0))
        assertEquals("false", variableValue.candidates.get(1))
    }
}

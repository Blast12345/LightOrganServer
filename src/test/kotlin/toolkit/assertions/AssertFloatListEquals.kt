package toolkit.assertions

import org.junit.jupiter.api.Assertions.assertEquals

fun assertFloatListEquals(expected: List<Float>, actual: List<Float>, precision: Float) {
    assertListLengthsAreEqual(expected, actual)
    assertEachFloatIsEqual(expected, actual, precision)
}

private fun assertEachFloatIsEqual(expected: List<Float>, actual: List<Float>, precision: Float) {
    expected.forEachIndexed { index, expectedValue ->
        val actualValue = actual[index]
        assertEquals(expectedValue, actualValue, precision)
    }
}
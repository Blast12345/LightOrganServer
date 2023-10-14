package toolkit.assertions

import org.junit.jupiter.api.Assertions
import kotlin.math.abs

fun assertFloatListEquals(expected: List<Float>, actual: List<Float>, precision: Float) {
    Assertions.assertEquals(expected.size, actual.size, "Lists have different sizes")

    for (i in expected.indices) {
        val expectedValue = expected[i]
        val actualValue = actual[i]
        Assertions.assertTrue(
            abs(expectedValue - actualValue) <= precision,
            "Values at index $i differ more than $precision: expected $expectedValue but was $actualValue"
        )
    }
}
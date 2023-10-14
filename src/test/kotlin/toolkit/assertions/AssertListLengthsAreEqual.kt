package toolkit.assertions

import org.junit.jupiter.api.Assertions

fun <T> assertListLengthsAreEqual(expected: List<T>, actual: List<T>) {
    val lengthOfExpected = expected.size
    val lengthOfActual = actual.size
    val failureMessage = "List are not the same length. Expected length of $lengthOfExpected but was $lengthOfActual."
    Assertions.assertEquals(lengthOfExpected, lengthOfActual, failureMessage)
}
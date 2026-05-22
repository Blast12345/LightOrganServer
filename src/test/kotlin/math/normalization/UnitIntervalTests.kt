package math.normalization

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UnitIntervalTests {

    // Valid value
    @Test
    fun `a value between zero and one is valid unit interval`() {
        val actual = UnitInterval(0.5)

        assertEquals(0.5, actual.value, 0.001)
    }

    @Test
    fun `zero is a valid unit interval`() {
        val actual = UnitInterval(0.0)

        assertEquals(0.0, actual.value, 0.001)
    }

    @Test
    fun `one is a valid unit interval`() {
        val actual = UnitInterval(1.0)

        assertEquals(1.0, actual.value, 0.001)
    }

    // Invalid value
    @Test
    fun `unit interval cannot be less than zero`() {
        assertThrows<Exception> { UnitInterval(-0.1) }
    }

    @Test
    fun `unit interval cannot be greater than one`() {
        assertThrows<Exception> { UnitInterval(1.1) }
    }

    // Clamping
    @Test
    fun `given a value less than zero, clamping clips to zero`() {
        val actual = UnitInterval.clamped(-0.1)

        assertEquals(0.0, actual.value, 0.001)
    }

    @Test
    fun `given a value greater than one, clamping clips to one`() {
        val actual = UnitInterval.clamped(1.1)

        assertEquals(1.0, actual.value, 0.001)
    }

    @Test
    fun `clamping a value already within range passes it through unchanged`() {
        val actual = UnitInterval.clamped(0.5)

        assertEquals(0.5, actual.value, 0.001)
    }

}
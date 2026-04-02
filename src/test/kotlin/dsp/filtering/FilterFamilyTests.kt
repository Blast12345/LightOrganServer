package dsp.filtering

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextFilterOrder

class FilterFamilyTests {

    private val order = nextFilterOrder()

    @Test
    fun `the rolloff ratio for a Butterworth Filter is 1 at -3 dB`() {
        val family = FilterFamily.Butterworth(order)

        val result = family.rolloffRatio(-3f)

        assertEquals(1.0f, result, 0.01f)
    }

}
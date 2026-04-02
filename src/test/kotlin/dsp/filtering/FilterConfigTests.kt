package dsp.filtering

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FilterConfigTests {

    private val cutoffFrequency = 1000f
    private val dbPerOctave = -12
    private val family = FilterFamily.Butterworth(FilterOrder.fromDbPerOctave(dbPerOctave))

    // Filter types (just verifying general direction)
    @Test
    fun `low pass frequency at magnitude attenuates from cutoff`() {
        val config = FilterConfig(
            type = FilterType.LowPass(frequency = 1000f),
            family = family
        )

        val result = config.frequencyAt(dbPerOctave.toFloat())

        assertTrue(result > cutoffFrequency)
    }

    @Test
    fun `high pass frequency at magnitude attenuates from cutoff`() {
        val config = FilterConfig(
            type = FilterType.HighPass(frequency = 1000f),
            family = family
        )

        val result = config.frequencyAt(dbPerOctave.toFloat())

        assertTrue(result < cutoffFrequency)
    }

}
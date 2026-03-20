package dsp.filtering.config

import dsp.filtering.ButterworthHighPass
import dsp.filtering.ButterworthLowPass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextPositiveInt

class FilterBuilderTests {

    private val sampleRate = 44100f
    private val frequency = 1000f
    private val order = nextPositiveInt()

    @Test
    fun `build a Butterworth low pass filter`() {
        val sut = FilterBuilder()

        val config = FilterConfig.lowPass(FilterFamily.BUTTERWORTH, frequency, order)
        val filter = sut.build(config, sampleRate)

        assertTrue(filter is ButterworthLowPass)
        assertEquals(sampleRate, filter.sampleRate)
        assertEquals(order, filter.order)
    }

    @Test
    fun `build a Butterworth high pass filter`() {
        val sut = FilterBuilder()

        val config = FilterConfig.highPass(FilterFamily.BUTTERWORTH, frequency, order)
        val filter = sut.build(config, sampleRate)

        assertTrue(filter is ButterworthHighPass)
        assertEquals(sampleRate, filter.sampleRate)
        assertEquals(order, filter.order)
    }

}
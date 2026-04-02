package dsp.filtering.primitives

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextPositiveFloat

class OnePoleOneZeroFilterTests {

    val sampleRate = nextPositiveFloat()

    @Test
    fun `order is 1`() {
        val filter = OnePoleOneZeroFilter(sampleRate, b0 = 1.0, b1 = 0.0, a1 = 0.0)

        assertEquals(1, filter.order)
    }

    @Test
    fun `get the sample rate supported by the filter`() {
        val filter = OnePoleOneZeroFilter(sampleRate, b0 = 0.0, b1 = 0.0, a1 = 0.0)

        assertEquals(sampleRate, filter.sampleRate)
    }

    @Test
    fun `current input scaled by b0`() {
        val filter = OnePoleOneZeroFilter(sampleRate, b0 = 0.5, b1 = 0.0, a1 = 0.0)
        val input = floatArrayOf(1f, 2f, 3f, 4f)

        val output = filter.filter(input)

        assertArrayEquals(floatArrayOf(0.5f, 1f, 1.5f, 2f), output)
    }

    @Test
    fun `input from one step ago is scaled by b1`() {
        val filter = OnePoleOneZeroFilter(sampleRate, b0 = 0.0, b1 = 1.0, a1 = 0.0)
        val input = floatArrayOf(1f, 2f, 3f, 4f)

        val output = filter.filter(input)

        assertArrayEquals(floatArrayOf(0f, 1f, 2f, 3f), output)
    }

    @Test
    fun `output from one step ago is scaled by a1`() {
        val filter = OnePoleOneZeroFilter(sampleRate, b0 = 1.0, b1 = 0.0, a1 = -0.5)
        val input = floatArrayOf(1f, 0f, 0f, 0f)

        val output = filter.filter(input)

        // y[0] = 1.0
        // y[1] = 0 - (-0.5 * 1.0) = 0.5
        // y[2] = 0 - (-0.5 * 0.5) = 0.25
        // y[3] = 0 - (-0.5 * 0.25) = 0.125
        assertArrayEquals(floatArrayOf(1f, 0.5f, 0.25f, 0.125f), output)
    }

    @Test
    fun `all coefficients combine additively`() {
        val filter = OnePoleOneZeroFilter(sampleRate, b0 = 1.0, b1 = 0.5, a1 = 0.0)
        val input = floatArrayOf(1f, 1f, 1f)

        val output = filter.filter(input)

        // y[0] = 1.0 * 1 + 0.5 * 0 = 1.0
        // y[1] = 1.0 * 1 + 0.5 * 1 = 1.5
        // y[2] = 1.0 * 1 + 0.5 * 1 = 1.5
        assertArrayEquals(floatArrayOf(1f, 1.5f, 1.5f), output)
    }

    @Test
    fun `state carries across consecutive calls`() {
        val filter = OnePoleOneZeroFilter(sampleRate, b0 = 0.0, b1 = 1.0, a1 = 0.0)

        filter.filter(floatArrayOf(1f, 2f))
        val output = filter.filter(floatArrayOf(5f, 6f))

        // the first output uses the last sample from the previous call
        assertArrayEquals(floatArrayOf(2f, 5f), output)
    }

}
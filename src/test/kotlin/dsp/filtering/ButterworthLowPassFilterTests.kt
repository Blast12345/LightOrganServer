package dsp.filtering

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import toolkit.generators.generateSineWave
import toolkit.monkeyTest.nextPositiveInt

class ButterworthLowPassTests {

    private val sampleRate = 44100f
    private val frequency = 1000f

    private val toneOneOctaveDown = generateSineWave(frequency / 2f, sampleRate)
    private val tone = generateSineWave(frequency, sampleRate)
    private val toneOneOctaveUp = generateSineWave(frequency * 2f, sampleRate)

    @Test
    fun `get the order of the filter`() {
        val order = nextPositiveInt()

        val filter = ButterworthLowPass(frequency, order, sampleRate)

        assertEquals(order, filter.order)
    }

    @Test
    fun `get the sample rate supported by the filter`() {
        val filter = ButterworthLowPass(frequency, order = 4, sampleRate)

        assertEquals(sampleRate, filter.supportedSampleRate)
    }

    @Test
    fun `order must be at least 1`() {
        assertThrows<IllegalArgumentException> {
            ButterworthLowPass(frequency, order = 0, sampleRate)
        }
    }

    @Test
    fun `passes signal below cutoff`() {
        val filter = ButterworthLowPass(frequency, order = 4, sampleRate)

        val output = filter.filter(toneOneOctaveDown.samples)

        val magnitude = measureMagnitude(output)
        assertEquals(1f, magnitude, 0.05f)
    }

    @Test
    fun `minus 3 dB at cutoff`() {
        val filter = ButterworthLowPass(frequency, order = 4, sampleRate)

        val output = filter.filter(tone.samples)

        val magnitude = measureMagnitude(output)
        assertEquals(-3f, magnitudeToDb(magnitude), 0.5f)
    }

    @Test
    fun `rolls off at 6 dB per octave per order`() {
        val filter1 = ButterworthLowPass(frequency, order = 1, sampleRate)
        val output1 = filter1.filter(toneOneOctaveUp.samples)
        val magnitude1 = measureMagnitude(output1)
        assertEquals(-6f, magnitudeToDb(magnitude1), 1.5f)

        val filter2 = ButterworthLowPass(frequency, order = 2, sampleRate)
        val output2 = filter2.filter(toneOneOctaveUp.samples)
        val magnitude2 = measureMagnitude(output2)
        assertEquals(-12f, magnitudeToDb(magnitude2), 1.5f)
    }
}
package dsp.filtering

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import toolkit.generators.generateSineWave

class ButterworthHighPassTests {

    private val sampleRate = 44100f
    private val frequency = 1000f

    private val toneOneOctaveDown = generateSineWave(frequency / 2f, sampleRate)
    private val tone = generateSineWave(frequency, sampleRate)
    private val toneOneOctaveUp = generateSineWave(frequency * 2f, sampleRate)

    @Test
    fun `get the sample rate supported by the filter`() {
        val filter = ButterworthHighPass(frequency, order = 4, sampleRate)

        assertEquals(sampleRate, filter.supportedSampleRate)
    }

    @Test
    fun `order must be at least 1`() {
        assertThrows<IllegalArgumentException> {
            ButterworthHighPass(frequency, order = 0, sampleRate)
        }
    }

    @Test
    fun `passes signal above cutoff`() {
        val filter = ButterworthHighPass(frequency, order = 4, sampleRate)

        val output = filter.filter(toneOneOctaveUp.samples)

        val magnitude = measureMagnitude(output)
        assertEquals(1f, magnitude, 0.05f)
    }

    @Test
    fun `minus 3 dB at cutoff`() {
        val filter = ButterworthHighPass(frequency, order = 4, sampleRate)

        val output = filter.filter(tone.samples)

        val magnitude = measureMagnitude(output)
        assertEquals(-3f, magnitudeToDb(magnitude), 0.5f)
    }

    @Test
    fun `rolls off at 6 dB per octave per order`() {
        val filter1 = ButterworthHighPass(frequency, order = 1, sampleRate)
        val output1 = filter1.filter(toneOneOctaveDown.samples)
        val magnitude1 = measureMagnitude(output1)
        assertEquals(-6f, magnitudeToDb(magnitude1), 1.5f)

        val filter2 = ButterworthHighPass(frequency, order = 2, sampleRate)
        val output2 = filter2.filter(toneOneOctaveDown.samples)
        val magnitude2 = measureMagnitude(output2)
        assertEquals(-12f, magnitudeToDb(magnitude2), 1.5f)
    }

}
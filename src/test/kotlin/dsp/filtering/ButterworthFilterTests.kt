package dsp.filtering

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import toolkit.generators.generateSineWave
import kotlin.math.abs
import kotlin.math.log10


class ButterworthFilterTests {

    private val sampleRate = 44100f
    private val frequency = 1000f

    private val toneOneOctaveDown = generateSineWave(frequency / 2f, sampleRate)
    private val tone = generateSineWave(frequency, sampleRate)
    private val toneOneOctaveUp = generateSineWave(frequency * 2, sampleRate)

    // General
    @Test
    fun `get the sample rate supported by the filter`() {
        val lowPass = ButterworthFilter.lowPass(frequency, order = 4, sampleRate)
        val highPass = ButterworthFilter.highPass(frequency, order = 4, sampleRate)

        assertEquals(sampleRate, lowPass.supportedSampleRate)
        assertEquals(sampleRate, highPass.supportedSampleRate)
    }

    @Test
    fun `order must be at least 1`() {
        assertThrows<IllegalArgumentException> {
            ButterworthFilter.lowPass(frequency, order = 0, sampleRate)
        }
        assertThrows<IllegalArgumentException> {
            ButterworthFilter.highPass(frequency, order = 0, sampleRate)
        }
    }

    // Low Pass
    @Test
    fun `low pass passes signal below cutoff`() {
        val filter = ButterworthFilter.lowPass(frequency, order = 4, sampleRate)

        val output = filter.filter(toneOneOctaveDown.samples)

        val magnitude = measureMagnitude(output)
        assertEquals(1f, magnitude, 0.05f)
    }

    @Test
    fun `low pass is at minus 3 dB at cutoff`() {
        val filter = ButterworthFilter.lowPass(frequency, order = 4, sampleRate)

        val output = filter.filter(tone.samples)

        val magnitude = measureMagnitude(output)
        assertEquals(-3f, magnitudeToDb(magnitude), 0.5f)
    }

    @Test
    fun `low pass rolls off at 6 dB per octave per order`() {
        val filter1 = ButterworthFilter.lowPass(frequency, order = 1, sampleRate)
        val output1 = filter1.filter(toneOneOctaveUp.samples)
        val magnitude1 = measureMagnitude(output1)
        assertEquals(-6f, magnitudeToDb(magnitude1), 1.5f)


        val filter2 = ButterworthFilter.lowPass(frequency, order = 2, sampleRate)
        val output2 = filter2.filter(toneOneOctaveUp.samples)
        val magnitude2 = measureMagnitude(output2)
        assertEquals(-12f, magnitudeToDb(magnitude2), 1.5f)
    }

    // High Pass
    @Test
    fun `high pass passes signal above cutoff`() {
        val filter = ButterworthFilter.highPass(frequency, order = 4, sampleRate)

        val output = filter.filter(toneOneOctaveUp.samples)

        val magnitude = measureMagnitude(output)
        assertEquals(1f, magnitude, 0.05f)
    }

    @Test
    fun `high pass is at minus 3 dB at cutoff`() {
        val filter = ButterworthFilter.highPass(frequency, order = 4, sampleRate)

        val output = filter.filter(tone.samples)

        val magnitude = measureMagnitude(output)
        assertEquals(-3f, magnitudeToDb(magnitude), 0.5f)
    }

    @Test
    fun `high pass rolls off at 6 dB per octave per order`() {
        val filter1 = ButterworthFilter.highPass(frequency, order = 1, sampleRate)
        val output1 = filter1.filter(toneOneOctaveDown.samples)
        val magnitude1 = measureMagnitude(output1)
        assertEquals(-6f, magnitudeToDb(magnitude1), 1.5f)


        val filter2 = ButterworthFilter.highPass(frequency, order = 2, sampleRate)
        val output2 = filter2.filter(toneOneOctaveDown.samples)
        val magnitude2 = measureMagnitude(output2)
        assertEquals(-12f, magnitudeToDb(magnitude2), 1.5f)
    }

    // Helpers

    // NOTE: Filters produce a transient when starting from the initial state.
    // Skip the first portion of the output to let the filter settle, then measure the peak of the steady signal.
    fun measureMagnitude(
        samples: FloatArray,
        skipFraction: Float = 0.5f
    ): Float {
        val start = (samples.size * skipFraction).toInt()
        return samples.drop(start).maxOf { abs(it) }
    }

    private fun magnitudeToDb(magnitude: Float): Float {
        return 20f * log10(magnitude)
    }

}
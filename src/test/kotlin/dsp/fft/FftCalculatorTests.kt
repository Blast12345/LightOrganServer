package dsp.fft

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import toolkit.generators.generateSineWave
import kotlin.time.Duration.Companion.seconds

class FftCalculatorTests {

    private val sut = FftCalculator()

    val tone = generateSineWave(
        frequency = 10f,
        amplitude = 1f,
        duration = 1.seconds,
        sampleRate = 1024f, // This gives us 1 Hz spacing
    )

    val silence = generateSineWave(
        frequency = 10f,
        amplitude = 0f,
        duration = 1.seconds,
        sampleRate = 1024f,
    )

    @Test
    fun `single frequency sine wave produces peak at that frequency`() {
        val magnitudes = sut.calculateMagnitudes(tone.samples)

        val peakIndex = magnitudes.indices.maxBy { magnitudes[it] }
        assertEquals(10, peakIndex)
    }

    @Test
    fun `magnitudes are non-negative`() {
        val magnitudes = sut.calculateMagnitudes(tone.samples)

        magnitudes.forEach { assertTrue(it >= 0f) }
    }

    @Test
    fun `returns one magnitude per frequency bin`() {
        val magnitudes = sut.calculateMagnitudes(tone.samples)

        assertEquals(tone.samples.size / 2, magnitudes.size)
    }

    @Test
    fun `silence produces zero magnitudes`() {
        val magnitudes = sut.calculateMagnitudes(silence.samples)

        magnitudes.forEach { assertEquals(0f, it, 0.001f) }
    }

}

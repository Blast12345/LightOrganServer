package dsp.fft

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import toolkit.generators.generateSineWave

class FftCalculatorTests {

    private val sut = FftCalculator()

    val tone = generateSineWave(
        frequency = 10f,
        amplitude = 1f,
        sampleRate = 1024f,
        sampleSize = 1024, // This gives us 1 Hz spacing
    )

    @Test
    fun `single frequency sine wave produces peak at that frequency`() {
        val magnitudes = sut.calculateMagnitudes(tone)

        val peakIndex = magnitudes.indices.maxBy { magnitudes[it] }
        assertEquals(10, peakIndex)
    }

    @Test
    fun `magnitudes are non-negative`() {
        val magnitudes = sut.calculateMagnitudes(tone)

        magnitudes.forEach { assertTrue(it >= 0f) }
    }

    @Test
    fun `returns one magnitude per frequency bin`() {
        val magnitudes = sut.calculateMagnitudes(tone)

        assertEquals(tone.size / 2, magnitudes.size)
    }

    @Test
    fun `silence produces zero magnitudes`() {
        val silence = FloatArray(1024) { 0f }

        val magnitudes = sut.calculateMagnitudes(silence)

        magnitudes.forEach { assertEquals(0f, it, 0.001f) }
    }


}

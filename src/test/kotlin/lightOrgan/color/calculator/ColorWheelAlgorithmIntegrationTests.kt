package lightOrgan.color.calculator

import dsp.peakExtraction.SpectralPeak
import lightOrgan.color.ColorWheelAlgorithm
import math.perception.StevensPower
import music.WesternTuningSystem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.pow

class ColorWheelAlgorithmIntegrationTests {

    private val westernTuning = WesternTuningSystem()
    private val cFrequency = westernTuning.getFrequency(westernTuning.C, octave = 4)
    private val fSharpFrequency = westernTuning.getFrequency(westernTuning.F_SHARP, octave = 4)
    private val halfLoudness = loudnessToMagnitude(0.5)
    private val fullLoudness = loudnessToMagnitude(1.0)

    fun loudnessToMagnitude(loudness: Double): Float {
        return loudness.pow(1.0 / StevensPower.LOUDNESS_3KHZ_TONE.exponent).toFloat()
    }

    private fun createSUT(): ColorWheelAlgorithm {
        return ColorWheelAlgorithm(
            tuning = westernTuning
        )
    }

    @Test
    fun `given no peaks, then black is returned`() {
        val sut = createSUT()

        val actual = sut.calculate(listOf())

        assertEquals(0.0, actual.red.value, 0.001)
        assertEquals(0.0, actual.green.value, 0.001)
        assertEquals(0.0, actual.blue.value, 0.001)
    }

    @Test
    fun `given a C note at half loudness, then red at half brightness is returned`() {
        val sut = createSUT()
        val peak = SpectralPeak(cFrequency, halfLoudness)

        val actual = sut.calculate(listOf(peak))

        assertEquals(0.5, actual.red.value, 0.001)
        assertEquals(0.0, actual.green.value, 0.001)
        assertEquals(0.0, actual.blue.value, 0.001)
    }

    @Test
    fun `given a F# note at half loudness, then teal at half brightness is returned`() {
        val sut = createSUT()
        val peak = SpectralPeak(fSharpFrequency, halfLoudness)

        val actual = sut.calculate(listOf(peak))

        assertEquals(0.0, actual.red.value, 0.001)
        assertEquals(0.5, actual.green.value, 0.001)
        assertEquals(0.5, actual.blue.value, 0.001)
    }

    @Test
    fun `given C and F# notes each at half loudness, then white at slightly greater than half brightness is returned`() {
        val sut = createSUT()
        val peak1 = SpectralPeak(cFrequency, halfLoudness)
        val peak2 = SpectralPeak(fSharpFrequency, halfLoudness)

        val actual = sut.calculate(listOf(peak1, peak2))

        // analogous to a ~3 dB gain
        assertEquals(0.630, actual.red.value, 0.001)
        assertEquals(0.630, actual.green.value, 0.001)
        assertEquals(0.630, actual.blue.value, 0.001)
    }

    @Test
    fun `given C and F# notes each at full loudness, then white at full brightness is returned`() {
        val sut = createSUT()
        val peak1 = SpectralPeak(cFrequency, fullLoudness)
        val peak2 = SpectralPeak(fSharpFrequency, fullLoudness)

        val actual = sut.calculate(listOf(peak1, peak2))

        // we can't have a brightness greater than 1, so clip
        assertEquals(1.0, actual.red.value, 0.001)
        assertEquals(1.0, actual.green.value, 0.001)
        assertEquals(1.0, actual.blue.value, 0.001)
    }

}
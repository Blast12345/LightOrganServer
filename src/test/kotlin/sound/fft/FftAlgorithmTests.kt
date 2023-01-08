package sound.fft

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import toolkit.generators.SineWaveGenerator

class FftAlgorithmTests {

    private val sampleRate = 48000F
    private val signalFrequency = 12000F
    private val sampleSize = 4
    private val signal = SineWaveGenerator(sampleRate).generate(signalFrequency, sampleSize)

    private fun createSUT(): FftAlgorithm {
        return FftAlgorithm()
    }

    @Test
    fun `extract the magnitudes from a signal`() {
        val sut = createSUT()
        val magnitudes = sut.calculateRelativeMagnitudes(signal)
        val expected = doubleArrayOf(0.0, 4.0)
        assertArrayEquals(expected, magnitudes, 0.001)
    }

    @Test
    fun `protect the input signal from modification by reference`() {
        val sut = createSUT()
        val input = doubleArrayOf(1.0, 2.0)
        sut.calculateRelativeMagnitudes(input)
        val expected = doubleArrayOf(1.0, 2.0)
        assertArrayEquals(expected, input, 0.001)
    }

}
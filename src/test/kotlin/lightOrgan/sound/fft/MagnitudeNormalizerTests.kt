package lightOrgan.sound.fft

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

class MagnitudeNormalizerTests {

    private val magnitudes = doubleArrayOf(2.0, 4.0)
    private val sampleSize = 4

    private fun createSUT(): MagnitudeNormalizer {
        return MagnitudeNormalizer()
    }

    @Test
    fun `normalize the magnitudes by dividing the magnitude by the sample size`() {
        val sut = createSUT()
        val normalizedMagnitudes = sut.normalize(magnitudes, sampleSize)
        val expected = doubleArrayOf(0.5, 1.0)
        assertArrayEquals(expected, normalizedMagnitudes, 0.001)
    }

}
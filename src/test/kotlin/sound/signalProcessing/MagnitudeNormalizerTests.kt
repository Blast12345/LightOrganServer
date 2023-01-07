package sound.signalProcessing

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

class MagnitudeNormalizerTests {

    private val magnitudes = doubleArrayOf(2.0, 4.0)
    private val sampleSize = 2

    private fun createSUT(): MagnitudeNormalizer {
        return MagnitudeNormalizer()
    }

    // TODO: Check the math on this
    @Test
    fun `normalize the magnitudes by dividing the magnitude by the sample size`() {
        val sut = createSUT()
        val normalizedMagnitudes = sut.normalize(magnitudes, sampleSize)
        val expected = doubleArrayOf(1.0, 2.0)
        assertArrayEquals(expected, normalizedMagnitudes, 0.001)
    }

}
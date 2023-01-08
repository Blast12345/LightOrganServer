package sound.signalProcessing.interpolator

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

class InterpolatorNormalizerTests {

    private val signal = doubleArrayOf(1.0, 2.0)
    private val originalSize = 1

    private fun createSUT(): InterpolatorNormalizer {
        return InterpolatorNormalizer()
    }

    @Test
    fun `normalize a signal that has gone through a interpolator by multiplying compensating for the new data`() {
        val sut = createSUT()
        val correctedSignal = sut.normalize(signal, originalSize)
        val expected = doubleArrayOf(2.0, 4.0)
        assertArrayEquals(expected, correctedSignal, 0.001)
    }

}
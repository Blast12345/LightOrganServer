package sound.signalProcessor.interpolator

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

class ZeroPaddingInterpolationCorrectorTests {

    private val samples = doubleArrayOf(1.0, 2.0, 0.0, 0.0)
    private val originalSize = 2

    private fun createSUT(): ZeroPaddingInterpolationCorrector {
        return ZeroPaddingInterpolationCorrector()
    }

    @Test
    fun `preserve the average magnitude of the interpolated samples`() {
        val sut = createSUT()

        val actual = sut.correct(samples, originalSize)

        val expected = doubleArrayOf(2.0, 4.0, 0.0, 0.0)
        assertArrayEquals(expected, actual, 0.001)
    }

}

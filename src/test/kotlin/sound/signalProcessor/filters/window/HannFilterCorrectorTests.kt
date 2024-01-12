package sound.signalProcessor.filters.window

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HannFilterCorrectorTests {

    private fun createSUT(): HannFilterCorrector {
        return HannFilterCorrector()
    }

    @Test
    fun `apply the window correction factor to a set of samples`() {
        val sut = createSUT()
        val windowedSamples = doubleArrayOf(1.0, 2.0, 3.0)

        val actual = sut.correct(windowedSamples)

        val expected = doubleArrayOf(2.0, 4.0, 6.0)
        Assertions.assertArrayEquals(expected, actual, 0.001)
    }

}

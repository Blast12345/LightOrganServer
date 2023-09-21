package sound.signalProcessor.filters.window

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

class HannFilterAlgorithmTests {

    private fun createSUT(): HannFilterAlgorithm {
        return HannFilterAlgorithm()
    }

    @Test
    fun `apply the hann filter to a set of samples`() {
        val sut = createSUT()
        val samples = doubleArrayOf(0.0, 1.1, 2.2, 3.3)

        val actual = sut.applyTo(samples)

        val expected = doubleArrayOf(0.0, 0.825, 1.65, 0.0)
        assertArrayEquals(expected, actual, 0.001)
    }

}
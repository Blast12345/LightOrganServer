package sound.signalProcessor.filters.latest

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

class LatestSamplesFilterTests {

    private val samples = doubleArrayOf(1.0, 2.0, 3.0)
    private val sampleSize = 2

    private fun createSUT(): LatestSamplesFilter {
        return LatestSamplesFilter(
            sampleSize = sampleSize
        )
    }

    @Test
    fun `take the latest N samples that support the lowest frequency`() {
        val sut = createSUT()
        val extractedSamples = sut.filter(samples)
        val expected = doubleArrayOf(2.0, 3.0)
        assertArrayEquals(expected, extractedSamples, 0.001)
    }

}
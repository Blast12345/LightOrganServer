package bins.frequency

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class PeakFrequencyBinsCalculatorTests {

    private fun createSUT(): PeakFrequencyBinsCalculator {
        return PeakFrequencyBinsCalculator()
    }

    // Single peak
    @Test
    fun `given there is a single symmetric peak, return that peak`() {
        val sut = createSUT()
        val bins = listOf(
            FrequencyBin(1f, 0.0f),
            FrequencyBin(2f, 1.0f),
            FrequencyBin(3f, 0.0f),
        )

        val result = sut.calculate(bins)

        assertEquals(1, result.size)
        assertEquals(2f, result.first().frequency, 0.001f)
        assertEquals(1.0f, result.first().magnitude, 0.001f)
    }

    @Test
    fun `given there is a single peak between two bins, return that peak`() {
        val sut = createSUT()
        val bins = listOf(
            FrequencyBin(1f, 0.5f),
            FrequencyBin(2f, 1.0f),
            FrequencyBin(3f, 1.0f),
        )

        val result = sut.calculate(bins)

        assertEquals(1, result.size)
        assertEquals(2.5f, result.first().frequency, 0.01f)
        assertEquals(1.09f, result.first().magnitude, 0.01f)
    }

    // Multiple peaks
    @Test
    fun `given multiple peaks, return those peaks`() {
        val sut = createSUT()

        val bins = listOf(
            FrequencyBin(1f, 1F),
            FrequencyBin(2f, 5F),
            FrequencyBin(3f, 1F),
            FrequencyBin(4f, 5F),
            FrequencyBin(5f, 1F)
        )

        val result = sut.calculate(bins)

        assertEquals(2, result.size)
        assertEquals(2f, result[0].frequency, 0.001f)
        assertEquals(5F, result[0].magnitude, 0.001f)
        assertEquals(4f, result[1].frequency, 0.001f)
        assertEquals(5F, result[1].magnitude, 0.001f)
    }

    // No peaks
    @Test
    fun `given all magnitudes are the same, return an empty list`() {
        val sut = createSUT()
        val magnitude = Random.nextFloat()

        val bins = listOf(
            FrequencyBin(1f, magnitude),
            FrequencyBin(2f, magnitude),
            FrequencyBin(3f, magnitude),
        )

        val result = sut.calculate(bins)

        assertEquals(0, result.size)
    }

    @Test
    fun `given all magnitudes increasing, return an empty list`() {
        val sut = createSUT()

        val bins = listOf(
            FrequencyBin(1f, 1f),
            FrequencyBin(2f, 2f),
            FrequencyBin(3f, 3f),
        )

        val result = sut.calculate(bins)

        assertEquals(0, result.size)
    }

    @Test
    fun `given all magnitudes decreasing, return an empty list`() {
        val sut = createSUT()

        val bins = listOf(
            FrequencyBin(1f, 3f),
            FrequencyBin(2f, 2f),
            FrequencyBin(3f, 1f),
        )

        val result = sut.calculate(bins)

        assertEquals(0, result.size)
    }

    // Empty input
    @Test
    fun `given an empty input, then return an empty list`() {
        val sut = createSUT()

        val result = sut.calculate(emptyList())

        assertEquals(0, result.size)
    }

    // Edge bins
    // NOTE: We cannot confidently identify a peak at an edge
    // E.g., a peak at 0.1 / 0.5 / etc. would all look like a peak at 1f
    @Test
    fun `given the only peak is at the lowest bin, then return an empty list`() {
        val sut = createSUT()
        val bins = listOf(
            FrequencyBin(1f, 1.0F),
            FrequencyBin(2f, 0.2F),
            FrequencyBin(3f, 0.1f)
        )

        val result = sut.calculate(bins)

        assertEquals(0, result.size)
    }

    @Test
    fun `given the only peak is at the highest bin, then return an empty list`() {
        val sut = createSUT()
        val bins = listOf(
            FrequencyBin(1f, 0.1F),
            FrequencyBin(2f, 0.2F),
            FrequencyBin(3f, 1.0f)
        )

        val result = sut.calculate(bins)

        assertEquals(0, result.size)
    }

    // Sorting - parabolic interpolation only works if the bins are in order
    @Test
    fun `given unsorted bins, peaks are still detected`() {
        val sut = createSUT()
        val bins = listOf(
            FrequencyBin(3f, 1.0f),
            FrequencyBin(1f, 0.5f),
            FrequencyBin(2f, 1.0f),
        )

        val result = sut.calculate(bins)

        assertEquals(1, result.size)
        assertEquals(2.5f, result.first().frequency, 0.01f)
        assertEquals(1.09f, result.first().magnitude, 0.01f)
    }

}
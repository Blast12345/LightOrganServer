package sound.frequencyBins.filters

import config.HighPassFilter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin

class BassFrequencyBinsFilterTests {

    private val highPassFilter = HighPassFilter(120F, 20F)

    private val bin1 = FrequencyBin(100F, 1F)
    private val bin2 = FrequencyBin(120F, 1F)
    private val bin3 = FrequencyBin(130F, 1F)
    private val bin4 = FrequencyBin(140F, 1F)
    private val frequencyBins = listOf(bin1, bin2, bin3, bin4)

    private val expectedBin1 = FrequencyBin(100F, 1F)
    private val expectedBin2 = FrequencyBin(120F, 1F)
    private val expectedBin3 = FrequencyBin(130F, 0.5F)
    private val expectedBin4 = FrequencyBin(140F, 0F)

    private fun createSUT(): BassFrequencyBinsFilter {
        return BassFrequencyBinsFilter(
            highPassFilter = highPassFilter
        )
    }

    @Test
    fun `return frequency bins that roll off from the highpass frequency`() {
        val sut = createSUT()
        val actual = sut.filter(frequencyBins)
        val expected = listOf(expectedBin1, expectedBin2, expectedBin3, expectedBin4)
        assertEquals(expected, actual)
    }

}
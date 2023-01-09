package sound.frequencyBins

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.filters.SupportedFrequencyBinsFilter

class SupportedFrequencyBinsFilterTests {

    private val lowestFrequency = 20F
    private val unsupportedBin = FrequencyBin(19F, 1F)
    private val supportedBin1 = FrequencyBin(20F, 1F)
    private val supportedBin2 = FrequencyBin(21F, 1F)
    private val frequencyBins = listOf(unsupportedBin, supportedBin1, supportedBin2)

    private fun createSUT(): SupportedFrequencyBinsFilter {
        return SupportedFrequencyBinsFilter()
    }

    @Test
    fun `filter out bins below the lowest frequency`() {
        val sut = createSUT()
        val supportedFrequencyBins = sut.filter(frequencyBins, lowestFrequency)
        val expected = listOf(supportedBin1, supportedBin2)
        assertEquals(expected, supportedFrequencyBins)
    }

}
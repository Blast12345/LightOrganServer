package sound.frequencyBins.dominant.frequency

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin

class PeakFrequencyBinsFinderTests {

    private val bin1 = FrequencyBin(10F, 1F)
    private val bin2 = FrequencyBin(20F, 2F)
    private val bin3 = FrequencyBin(30F, 1F)
    private val bin4 = FrequencyBin(40F, 3F)
    private val bin5 = FrequencyBin(50F, 2F)
    private val frequencyBins = listOf(bin1, bin2, bin3, bin4, bin5)

    private fun createSUT(): PeakFrequencyBinsFinder {
        return PeakFrequencyBinsFinder()
    }

    @Test
    fun `find the peak frequency bins`() {
        val sut = createSUT()
        val frequencyBins = sut.find(frequencyBins)
        val expected = listOf(bin2, bin4)
        assertEquals(expected, frequencyBins)
    }

}
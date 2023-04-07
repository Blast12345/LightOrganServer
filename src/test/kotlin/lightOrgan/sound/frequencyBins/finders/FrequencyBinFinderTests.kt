package lightOrgan.sound.frequencyBins.finders

import lightOrgan.sound.frequencyBins.FrequencyBin
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrequencyBinFinderTests {

    private val bin1 = FrequencyBin(10F, 1F)
    private val bin2 = FrequencyBin(20F, 2F)
    private val bin3 = FrequencyBin(30F, 1F)
    private val bin4 = FrequencyBin(40F, 3F)
    private val bin5 = FrequencyBin(50F, 2F)
    private val frequencyBins = listOf(bin1, bin2, bin3, bin4, bin5)

    private fun createSUT(): FrequencyBinFinder {
        return FrequencyBinFinder()
    }

    @Test
    fun `find the peak frequency bins`() {
        val sut = createSUT()
        val frequencyBins = sut.findPeaks(frequencyBins)
        val expected = listOf(bin2, bin4)
        assertEquals(expected, frequencyBins)
    }

}
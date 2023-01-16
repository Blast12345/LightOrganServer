package sound.frequencyBins.filters

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin

class BassFrequencyBinsFilterTests {

    private val bin1 = FrequencyBin(119F, 0F)
    private val bin2 = FrequencyBin(120F, 0F)
    private val bin3 = FrequencyBin(121F, 0F)
    private val frequencyBins = listOf(bin1, bin2, bin3)


    private fun createSUT(): BassFrequencyBinsFilter {
        return BassFrequencyBinsFilter()
    }

    @Test
    fun `return frequency bins of 120hz or less`() {
        val sut = createSUT()
        val actual = sut.filter(frequencyBins)
        val expected = listOf(bin1, bin2)
        assertEquals(expected, actual)
    }

}
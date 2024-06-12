package sound.signalProcessor

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.bins.frequency.FrequencyBin

class FrequencyBinsRangeFilterTests {

    private val frequencyBin10 = FrequencyBin(10F, 1F)
    private val frequencyBin20 = FrequencyBin(20F, 1F)
    private val frequencyBin30 = FrequencyBin(30F, 1F)
    private val frequencyBin40 = FrequencyBin(40F, 1F)
    private val frequencyBin50 = FrequencyBin(50F, 1F)
    private val frequencyBins = listOf(
        frequencyBin10,
        frequencyBin20,
        frequencyBin30,
        frequencyBin40,
        frequencyBin50
    )

    private fun createSUT(): FrequencyBinsRangeFilter {
        return FrequencyBinsRangeFilter()
    }

    @Test
    fun `get the frequency bins within the specified range`() {
        val sut = createSUT()

        val actual = sut.filter(
            frequencyBins = frequencyBins,
            lowestFrequency = 20F,
            highestFrequency = 40F
        )

        val expected = listOf(
            frequencyBin20,
            frequencyBin30,
            frequencyBin40
        )
        assertEquals(expected, actual)
    }

}

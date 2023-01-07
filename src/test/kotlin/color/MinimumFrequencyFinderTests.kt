package color

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList

class MinimumFrequencyFinderTests {

    private val bin1 = FrequencyBin(100F, 1F)
    private val bin2 = FrequencyBin(200F, 2F)
    private val bin3 = FrequencyBin(300F, 3F)

    private fun createSUT(): MinimumFrequencyFinder {
        return MinimumFrequencyFinder()
    }

    @Test
    fun `find the minimum frequency in a list of sorted bins`() {
        val sut = createSUT()
        val sortedList = arrayListOf(bin1, bin2, bin3)
        val minimumFrequency = sut.find(sortedList)
        assertEquals(100.0F, minimumFrequency!!, 0.1F)
    }

    @Test
    fun `the minimum frequency is null when the frequency bin list is empty`() {
        val sut = createSUT()
        val emptyList: FrequencyBinList = emptyList()
        val minimumFrequency = sut.find(emptyList)
        assertNull(minimumFrequency)
    }

    @Test
    fun `find the minimum frequency in a list of unsorted bins`() {
        val sut = createSUT()
        val unsortedList = arrayListOf(bin3, bin1, bin2)
        val minimumFrequency = sut.find(unsortedList)
        assertEquals(100.0F, minimumFrequency!!, 0.1F)
    }

}
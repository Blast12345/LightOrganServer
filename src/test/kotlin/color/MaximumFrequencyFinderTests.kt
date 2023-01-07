package color

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList

class MaximumFrequencyFinderTests {

    private val bin1 = FrequencyBin(100F, 1F)
    private val bin2 = FrequencyBin(200F, 2F)
    private val bin3 = FrequencyBin(300F, 3F)

    private fun createSUT(): MaximumFrequencyFinder {
        return MaximumFrequencyFinder()
    }

    @Test
    fun `find the maximum frequency in a list of sorted bins`() {
        val sut = createSUT()
        val sortedList = arrayListOf(bin1, bin2, bin3)
        val maximumFrequency = sut.find(sortedList)
        assertEquals(300.0F, maximumFrequency!!, 0.1F)
    }

    @Test
    fun `the maximum frequency is null when the frequency bin list is empty`() {
        val sut = createSUT()
        val emptyList: FrequencyBinList = emptyList()
        val maximumFrequency = sut.find(emptyList)
        assertNull(maximumFrequency)
    }

    @Test
    fun `find the maximum frequency in a list of unsorted bins`() {
        val sut = createSUT()
        val unsortedList = arrayListOf(bin3, bin1, bin2)
        val maximumFrequency = sut.find(unsortedList)
        assertEquals(300.0F, maximumFrequency!!, 0.1F)
    }

}
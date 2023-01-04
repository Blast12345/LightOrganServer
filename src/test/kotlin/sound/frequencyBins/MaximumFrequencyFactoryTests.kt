package sound.frequencyBins

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class MaximumFrequencyFactoryTests {

    private val bin1 = FrequencyBin(100F, 1.0)
    private val bin2 = FrequencyBin(200F, 2.0)
    private val bin3 = FrequencyBin(300F, 3.0)

    private fun createSUT(): MaximumFrequencyFactory {
        return MaximumFrequencyFactory()
    }

    @Test
    fun `get the maximum frequency from a list of sorted bins`() {
        val sut = createSUT()
        val sortedList = arrayListOf(bin1, bin2, bin3)
        val maximumFrequency = sut.maximumFrequency(sortedList)
        assertEquals(300.0F, maximumFrequency!!, 0.1F)
    }

    @Test
    fun `the maximum frequency is null when the frequency bin list is empty`() {
        val sut = createSUT()
        val emptyList: FrequencyBins = emptyList()
        val maximumFrequency = sut.maximumFrequency(emptyList)
        assertNull(maximumFrequency)
    }

    @Test
    fun `get the maximum frequency from a list of unsorted bins`() {
        val sut = createSUT()
        val unsortedList = arrayListOf(bin3, bin1, bin2)
        val maximumFrequency = sut.maximumFrequency(unsortedList)
        assertEquals(300.0F, maximumFrequency!!, 0.1F)
    }

}
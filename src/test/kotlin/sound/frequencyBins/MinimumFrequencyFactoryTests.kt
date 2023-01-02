package sound.frequencyBins

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class MinimumFrequencyFactoryTests {

    private val bin1 = FrequencyBin(100.0, 1.0)
    private val bin2 = FrequencyBin(200.0, 2.0)
    private val bin3 = FrequencyBin(300.0, 3.0)

    private fun createSUT(): MinimumFrequencyFactory {
        return MinimumFrequencyFactory()
    }

    @Test
    fun `get the minimum frequency from a list of sorted bins`() {
        val sut = createSUT()
        val sortedList = arrayListOf(bin1, bin2, bin3)
        val minimumFrequency = sut.minimumFrequencyFrom(sortedList)
        assertEquals(100.0F, minimumFrequency!!, 0.1F)
    }

    @Test
    fun `the minimum frequency is null when the frequency bin list is empty`() {
        val sut = createSUT()
        val emptyList: FrequencyBins = emptyList()
        val minimumFrequency = sut.minimumFrequencyFrom(emptyList)
        assertNull(minimumFrequency)
    }

    @Test
    fun `get the minimum frequency from a list of unsorted bins`() {
        val sut = createSUT()
        val unsortedList = arrayListOf(bin3, bin1, bin2)
        val minimumFrequency = sut.minimumFrequencyFrom(unsortedList)
        assertEquals(100.0F, minimumFrequency!!, 0.1F)
    }

}
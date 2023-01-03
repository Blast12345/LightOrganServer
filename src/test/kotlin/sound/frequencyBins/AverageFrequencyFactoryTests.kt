package sound.frequencyBins

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class AverageFrequencyFactoryTests {

    private val bin1 = FrequencyBin(100F, 0.0)
    private val bin2 = FrequencyBin(200F, 1.0)
    private val bin3 = FrequencyBin(300F, 2.0)

    private fun createSUT(): AverageFrequencyFactory {
        return AverageFrequencyFactory()
    }

    @Test
    fun `get the average frequency from a list of bins`() {
        val sut = createSUT()
        val list = arrayListOf(bin1, bin2, bin3)
        val averageFrequency = sut.averageFrequency(list)
        assertEquals(266.6F, averageFrequency!!, 0.1F)
    }

    @Test
    fun `the average frequency is null when the frequency bin list is empty`() {
        val sut = createSUT()
        val emptyList: FrequencyBins = emptyList()
        val averageFrequency = sut.averageFrequency(emptyList)
        assertNull(averageFrequency)
    }

    @Test
    fun `the average frequency is null when the frequency bins have no amplitude`() {
        val sut = createSUT()
        val listWithNoAmplitudes: FrequencyBins = listOf(bin1)
        val averageFrequency = sut.averageFrequency(listWithNoAmplitudes)
        assertNull(averageFrequency)
    }

}
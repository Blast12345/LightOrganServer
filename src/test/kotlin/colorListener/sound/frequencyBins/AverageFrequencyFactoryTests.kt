package colorListener.sound.frequencyBins

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AverageFrequencyFactoryTests {

    private val bin1 = FrequencyBin(100.0, 0.0)
    private val bin2 = FrequencyBin(200.0, 1.0)
    private val bin3 = FrequencyBin(300.0, 2.0)

    private fun createSUT(): AverageFrequencyFactory {
        return AverageFrequencyFactory()
    }

    @Test
    fun `get the average frequency from a list of bins`() {
        val sut = createSUT()
        val list = arrayListOf(bin1, bin2, bin3)
        val averageFrequency = sut.averageFrequencyFrom(list)
        assertEquals(266.6F, averageFrequency!!, 0.1F)
    }

    @Test
    fun `the average frequency is null when the frequency bin list is empty`() {
        val sut = createSUT()
        val emptyList: FrequencyBins = emptyList()
        val averageFrequency = sut.averageFrequencyFrom(emptyList)
        assertNull(averageFrequency)
    }

    @Test
    fun `the average frequency is null when the frequency bins have no amplitude`() {
        val sut = createSUT()
        val listWithNoAmplitudes: FrequencyBins = listOf(bin1)
        val averageFrequency = sut.averageFrequencyFrom(listWithNoAmplitudes)
        assertNull(averageFrequency)
    }

}
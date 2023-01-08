package color

import color.dominantFrequency.AverageFrequencyCalculator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList

class AverageFrequencyCalculatorTests {

    private val bin1 = FrequencyBin(100F, 0F)
    private val bin2 = FrequencyBin(200F, 1F)
    private val bin3 = FrequencyBin(300F, 2F)

    private fun createSUT(): AverageFrequencyCalculator {
        return AverageFrequencyCalculator()
    }

    @Test
    fun `calculate the average frequency from a list of bins`() {
        val sut = createSUT()
        val list = arrayListOf(bin1, bin2, bin3)
        val averageFrequency = sut.calculate(list)
        assertEquals(266.6F, averageFrequency!!, 0.1F)
    }

    @Test
    fun `the average frequency is null when the frequency bin list is empty`() {
        val sut = createSUT()
        val emptyList: FrequencyBinList = emptyList()
        val averageFrequency = sut.calculate(emptyList)
        assertNull(averageFrequency)
    }

    @Test
    fun `the average frequency is null when the frequency bins have no magnitude`() {
        val sut = createSUT()
        val listWithNoMagnitudes: FrequencyBinList = listOf(bin1)
        val averageFrequency = sut.calculate(listWithNoMagnitudes)
        assertNull(averageFrequency)
    }

}
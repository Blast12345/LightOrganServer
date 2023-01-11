package sound.frequencyBins.dominantFrequency

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin

class DominantFrequencyCalculatorTests {

    private val bin1 = FrequencyBin(10F, 1F)
    private val bin2 = FrequencyBin(20F, 2F)
    private val bin3 = FrequencyBin(30F, 1F)
    private val bin4 = FrequencyBin(40F, 1F)
    private val bin5 = FrequencyBin(50F, 1F)
    private val frequencyBins = listOf(bin1, bin2, bin3, bin4, bin5)


    private fun createSUT(): DominantFrequencyCalculator {
        return DominantFrequencyCalculator()
    }

    @Test
    fun `the dominant frequency is the bin with the highest magnitude`() {
        val sut = createSUT()
        val actual = sut.calculate(frequencyBins)
        assertEquals(20F, actual)
    }

}
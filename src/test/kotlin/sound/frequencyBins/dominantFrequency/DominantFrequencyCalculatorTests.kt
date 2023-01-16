package sound.frequencyBins.dominantFrequency

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin

class DominantFrequencyCalculatorTests {

    private val bin1 = FrequencyBin(10F, 0F)
    private val bin2 = FrequencyBin(20F, 4F)
    private val bin3 = FrequencyBin(30F, 1F)
    private val bin4 = FrequencyBin(40F, 0F)


    private fun createSUT(): DominantFrequencyCalculator {
        return DominantFrequencyCalculator()
    }

    @Test
    fun `return the weighted average of the frequencies`() {
        val sut = createSUT()
        val frequencyBins = listOf(bin1, bin2, bin3, bin4)
        val actual = sut.calculate(frequencyBins)
        assertEquals(22F, actual)
    }

    @Test
    fun `return null when the total magnitude is zero`() {
        val sut = createSUT()
        val frequencyBins = listOf(bin1, bin4)
        val actual = sut.calculate(frequencyBins)
        assertNull(actual)
    }

}
package sound.bins.dominant.frequency

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.bins.frequencyBins.FrequencyBin
import sound.bins.frequencyBins.dominant.frequency.TotalMagnitudeCalculator

class TotalMagnitudeCalculatorTests {

    private val bin1 = FrequencyBin(10F, 20F)
    private val bin2 = FrequencyBin(20F, 30F)
    private val frequencyBins = listOf(bin1, bin2)

    private fun createSUT(): TotalMagnitudeCalculator {
        return TotalMagnitudeCalculator()
    }

    @Test
    fun `calculate the total magnitude of frequency bins`() {
        val sut = createSUT()
        val actual = sut.calculate(frequencyBins)
        assertEquals(50F, actual)
    }

}
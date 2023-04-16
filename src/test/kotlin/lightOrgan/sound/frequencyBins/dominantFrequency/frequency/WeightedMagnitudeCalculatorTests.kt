package lightOrgan.sound.frequencyBins.dominantFrequency.frequency

import lightOrgan.sound.frequencyBins.FrequencyBin
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WeightedMagnitudeCalculatorTests {

    private val bin1 = FrequencyBin(10F, 20F)
    private val bin2 = FrequencyBin(20F, 30F)
    private val frequencyBins = listOf(bin1, bin2)

    private fun createSUT(): WeightedMagnitudeCalculator {
        return WeightedMagnitudeCalculator()
    }

    @Test
    fun `calculate the weighted magnitude of frequency bins`() {
        val sut = createSUT()
        val actual = sut.calculate(frequencyBins)
        assertEquals(800F, actual)
    }

}
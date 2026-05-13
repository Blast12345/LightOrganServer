package sound.bins.frequency.dominant.frequency

import dsp.bins.FrequencyBin
import org.apache.commons.math3.complex.Complex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WeightedMagnitudeCalculatorTests {

    private val bin1 = FrequencyBin(10F, Complex(20.0, 0.0))
    private val bin2 = FrequencyBin(20F, Complex(30.0, 0.0))
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

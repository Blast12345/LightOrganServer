package sound.bins.frequency.dominant.magnitude

import dsp.bins.FrequencyBin
import org.apache.commons.math3.complex.Complex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DominantMagnitudeCalculatorTests {

    private val frequencyBins = listOf(
        FrequencyBin(10F, Complex(0.5, 0.0)),
        FrequencyBin(100F, Complex(1.2, 0.0)),
        FrequencyBin(1000F, Complex(0.75, 0.0))
    )


    private fun createSUT(): DominantMagnitudeCalculator {
        return DominantMagnitudeCalculator()
    }

    @Test
    fun `calculate the dominant magnitude`() {
        val sut = createSUT()

        val actual = sut.calculate(frequencyBins)

        assertEquals(1.2F, actual)
    }


}

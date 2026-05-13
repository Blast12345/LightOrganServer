package sound.bins.frequency.dominant.magnitude

import dsp.bins.FrequencyBin
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DominantMagnitudeCalculatorTests {

    private val frequencyBins = listOf(
        FrequencyBin(10F, 0.5),
        FrequencyBin(100F, 1.2),
        FrequencyBin(1000F, 0.75)
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

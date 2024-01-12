package sound.bins.frequency.dominant.magnitude

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.bins.frequency.FrequencyBin

class DominantMagnitudeCalculatorTests {

    private val frequencyBinList = listOf(
        FrequencyBin(10F, 0.5F),
        FrequencyBin(100F, 1.2F),
        FrequencyBin(1000F, 0.75F)
    )


    private fun createSUT(): DominantMagnitudeCalculator {
        return DominantMagnitudeCalculator()
    }

    @Test
    fun `calculate the dominant magnitude`() {
        val sut = createSUT()

        val actual = sut.calculate(frequencyBinList)

        assertEquals(1.2F, actual)
    }


}

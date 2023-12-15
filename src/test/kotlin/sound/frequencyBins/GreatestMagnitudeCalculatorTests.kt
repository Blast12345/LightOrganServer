package sound.frequencyBins

import io.mockk.clearAllMocks
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextFrequencyBinList

class GreatestMagnitudeCalculatorTests {

    private val frequencyBins = nextFrequencyBinList()

    private fun createSUT(): GreatestMagnitudeCalculator {
        return GreatestMagnitudeCalculator()
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    @Test
    fun `get the greatest magnitude from a list of frequency bins`() {
        val sut = createSUT()

        val actual = sut.calculate(frequencyBins)

        val expected = frequencyBins.maxOfOrNull { it.magnitude }
        assertEquals(expected, actual)
    }


}
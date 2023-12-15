package color.brightness

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.filters.BandPassFilter
import toolkit.monkeyTest.nextCrossover
import toolkit.monkeyTest.nextFrequencyBinList

class GreatestMagnitudeCalculatorTests {

    private val bandPassFilter: BandPassFilter = mockk()
    private val lowCrossover = nextCrossover()
    private val highCrossover = nextCrossover()
    private val frequencyBins = nextFrequencyBinList()
    private val filteredFrequencyBins = nextFrequencyBinList()

    private fun createSUT(): GreatestMagnitudeCalculator {
        return GreatestMagnitudeCalculator(
            bandPassFilter = bandPassFilter,
            lowCrossover = lowCrossover,
            highCrossover = highCrossover
        )
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    @Test
    fun `get the greatest magnitude from a list of frequency bins`() {
        val sut = createSUT()
        every { bandPassFilter.filter(frequencyBins, lowCrossover, highCrossover) } returns filteredFrequencyBins

        val actual = sut.calculate(frequencyBins)

        val expected = filteredFrequencyBins.maxOfOrNull { it.magnitude }
        assertEquals(expected, actual)
    }


}
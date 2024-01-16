package sound.bins.frequency.filters

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextCrossover
import toolkit.monkeyTest.nextFrequencyBins

class BandPassFilterTests {

    private val frequencyBins = nextFrequencyBins()
    private val lowCrossover = nextCrossover()
    private val highCrossover = nextCrossover()
    private val crossoverFilter: CrossoverFilter = mockk()
    private val singleCrossoverFrequencyBins = nextFrequencyBins()
    private val combinedCrossoverFrequencyBins = nextFrequencyBins()

    private fun createSUT(): BandPassFilter {
        return BandPassFilter(
            crossoverFilter = crossoverFilter
        )
    }

    @Test
    fun `filtered bins have an upper and lower crossover applied`() {
        val sut = createSUT()
        every { crossoverFilter.filter(frequencyBins, lowCrossover) } returns singleCrossoverFrequencyBins
        every { crossoverFilter.filter(singleCrossoverFrequencyBins, highCrossover) } returns combinedCrossoverFrequencyBins

        val actual = sut.filter(frequencyBins, lowCrossover, highCrossover)

        assertEquals(combinedCrossoverFrequencyBins, actual)
    }

}

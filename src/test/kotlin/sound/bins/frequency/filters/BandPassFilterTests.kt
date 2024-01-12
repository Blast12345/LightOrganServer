package sound.bins.frequency.filters

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.bins.frequency.filters.BandPassFilter
import sound.bins.frequency.filters.CrossoverFilter
import toolkit.monkeyTest.nextCrossover
import toolkit.monkeyTest.nextFrequencyBinList

class BandPassFilterTests {

    private val frequencyBinList = nextFrequencyBinList()
    private val lowCrossover = nextCrossover()
    private val highCrossover = nextCrossover()
    private val crossoverFilter: CrossoverFilter = mockk()
    private val singleCrossoverFrequencyBinList = nextFrequencyBinList()
    private val combinedCrossoverFrequencyBinList = nextFrequencyBinList()

    private fun createSUT(): BandPassFilter {
        return BandPassFilter(
            crossoverFilter = crossoverFilter
        )
    }

    @Test
    fun `filtered bins have an upper and lower crossover applied`() {
        val sut = createSUT()
        every { crossoverFilter.filter(frequencyBinList, lowCrossover) } returns singleCrossoverFrequencyBinList
        every { crossoverFilter.filter(singleCrossoverFrequencyBinList, highCrossover) } returns combinedCrossoverFrequencyBinList

        val actual = sut.filter(frequencyBinList, lowCrossover, highCrossover)

        assertEquals(combinedCrossoverFrequencyBinList, actual)
    }

}
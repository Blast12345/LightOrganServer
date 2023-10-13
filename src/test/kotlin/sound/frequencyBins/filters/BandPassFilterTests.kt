package sound.frequencyBins.filters

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextCrossover
import toolkit.monkeyTest.nextFrequencyBinList

class BandPassFilterTests {

    private val frequencyBinList = nextFrequencyBinList()
    private val lowCrossover = nextCrossover()
    private val highCrossover = nextCrossover()
    private val crossoverFilter: CrossoverFilter = mockk()
    private val lowCrossoverFrequencyBinList = nextFrequencyBinList()
    private val highCrossoverFrequencyBinList = nextFrequencyBinList()

    private fun createSUT(): BandPassFilter {
        return BandPassFilter(
            crossoverFilter = crossoverFilter
        )
    }

    @Test
    fun `upper and lower crossover filters are applied to produce the pass-band`() {
        val sut = createSUT()
        every { crossoverFilter.filter(frequencyBinList, lowCrossover) } returns lowCrossoverFrequencyBinList
        every { crossoverFilter.filter(lowCrossoverFrequencyBinList, highCrossover) } returns highCrossoverFrequencyBinList

        val actual = sut.filter(frequencyBinList, lowCrossover, highCrossover)

        // TODO: This test seems to lock in a specific order when one doesn't matter
        // At the very least, the idea that a "combined" bin list is returned is lost
        assertEquals(highCrossoverFrequencyBinList, actual)
    }

}
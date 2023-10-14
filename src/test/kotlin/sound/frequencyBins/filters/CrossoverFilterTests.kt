package sound.frequencyBins.filters

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin
import toolkit.assertions.assertFloatListEquals

class CrossoverFilterTests {

    private val bin10hz = FrequencyBin(10F, 1F)
    private val bin15hz = FrequencyBin(15F, 1F)
    private val bin20hz = FrequencyBin(20F, 1F)
    private val bin25hz = FrequencyBin(25F, 1F)
    private val bin30hz = FrequencyBin(30F, 1F)
    private val bins = listOf(bin10hz, bin15hz, bin20hz, bin25hz, bin30hz)
    private val crossover = Crossover(15F, 25F)

    private fun createSUT(): CrossoverFilter {
        return CrossoverFilter()
    }

    @Test
    fun `frequency bins beyond the stop band are removed`() {
        val sut = createSUT()

        val filteredBins = sut.filter(bins, crossover)
        val actualFrequencies = filteredBins.map { it.frequency }

        val expectedFrequencies = listOf(10F, 15F, 20F, 25F)
        assertEquals(expectedFrequencies, actualFrequencies)
    }

    @Test
    fun `the magnitude of the frequency bins between the corner frequency and stop frequency are rolled off`() {
        val sut = createSUT()

        val filteredBins = sut.filter(bins, crossover)
        val actualMagnitudes = filteredBins.map { it.magnitude }

        val expectedMagnitudes = listOf(1F, 1F, 0.43F, 0F)
        assertFloatListEquals(expectedMagnitudes, actualMagnitudes, 0.01F)
    }

}
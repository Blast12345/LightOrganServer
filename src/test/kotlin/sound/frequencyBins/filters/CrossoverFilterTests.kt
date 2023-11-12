package sound.frequencyBins.filters

import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin
import toolkit.assertions.assertFloatListEquals

class CrossoverFilterTests {

    private val bin10hz = FrequencyBin(10F, 1F)
    private val bin15hz = FrequencyBin(15F, 1F)
    private val bin20hz = FrequencyBin(20F, 1F)
    private val bin25hz = FrequencyBin(25F, 1F)
    private val bin30hz = FrequencyBin(30F, 1F)

    private fun createSUT(): CrossoverFilter {
        return CrossoverFilter()
    }

    @Test
    // NOTE: I opted for an integration test because I felt a unit test failed to express the behavior
    fun `filtered bins are rolled off from the corner frequency to the stop frequency`() {
        val sut = createSUT()

        val filteredBins = sut.filter(
            frequencyBinList = listOf(bin10hz, bin15hz, bin20hz, bin25hz, bin30hz),
            crossover = Crossover(15F, 25F)
        )

        val actualMagnitudes = filteredBins.map { it.magnitude }
        val expectedMagnitudes = listOf(1F, 1F, 0.43F, 0F, 0F)
        assertFloatListEquals(expectedMagnitudes, actualMagnitudes, 0.01F)
    }

}
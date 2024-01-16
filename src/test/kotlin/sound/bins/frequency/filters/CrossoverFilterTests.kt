package sound.bins.frequency.filters

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.bins.frequency.FrequencyBin

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
    fun `filtered bins are rolled off from the corner frequency to the stop frequency`() {
        val sut = createSUT()

        val actual = sut.filter(
            frequencyBins = listOf(bin10hz, bin15hz, bin20hz, bin25hz, bin30hz),
            crossover = Crossover(15F, 25F)
        )

        assertEquals(
            listOf(
                FrequencyBin(10F, 1F),
                FrequencyBin(15F, 1F),
                FrequencyBin(20F, 0.5F),
                FrequencyBin(25F, 0F),
                FrequencyBin(30F, 0F)
            ),
            actual
        )
    }

}

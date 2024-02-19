package gui.dashboard.tiles.spectrum

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.bins.frequency.FrequencyBin

class SpectrumCreatorTests {

    private val frequencyBin10 = FrequencyBin(10F, 0.0F)
    private val frequencyBin20 = FrequencyBin(20F, 0.5F)
    private val frequencyBin30 = FrequencyBin(30F, 1.0F)

    private fun createSUT(): SpectrumCreator {
        return SpectrumCreator()
    }

    @Test
    fun `create a frequency spectrum`() {
        val sut = createSUT()
        val frequencyBins = listOf(frequencyBin10, frequencyBin20, frequencyBin30)

        val spectrum = sut.create(frequencyBins, null)

        assertEquals(
            listOf(
                SpectrumBin(10F, 0.0F, false),
                SpectrumBin(20F, 0.5F, false),
                SpectrumBin(30F, 1.0F, false)
            ), spectrum
        )
    }

    @Test
    fun `the bin matching the hovered frequency is hovered`() {
        val sut = createSUT()
        val frequencyBins = listOf(frequencyBin10, frequencyBin20, frequencyBin30)

        val spectrum = sut.create(frequencyBins, hoveredFrequency = 20F)

        assertEquals(
            listOf(
                SpectrumBin(10F, 0.0F, false),
                SpectrumBin(20F, 0.5F, true),
                SpectrumBin(30F, 1.0F, false)
            ), spectrum
        )
    }

    @Test
    fun `magnitudes have a range of 0 to 1`() {
        val sut = createSUT()
        val lowFrequencyBin = FrequencyBin(10F, -2F)
        val highFrequencyBin = FrequencyBin(20F, 2F)
        val frequencyBins = listOf(lowFrequencyBin, highFrequencyBin)

        val spectrum = sut.create(frequencyBins, null)

        assertEquals(
            listOf(
                SpectrumBin(10F, 0F, false),
                SpectrumBin(20F, 1F, false)
            ), spectrum
        )
    }

}

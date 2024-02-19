package gui.dashboard.tiles.spectrum

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextFrequencyBins
import toolkit.monkeyTest.nextSpectrum
import toolkit.monkeyTest.nextSpectrumBin

class SpectrumTileViewModelTests {

    private val spectrumCreator: SpectrumCreator = mockk()
    private val spectrum = nextSpectrum()

    @BeforeEach
    fun setupHappyPath() {
        every { spectrumCreator.create(any(), any()) } returns spectrum
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): SpectrumTileViewModel {
        return SpectrumTileViewModel(
            spectrumCreator = spectrumCreator
        )
    }

    @Test
    fun `settings new frequency bins updates the spectrum`() {
        val sut = createSUT()
        val frequencyBins = nextFrequencyBins()

        sut.setFrequencyBins(frequencyBins)

        verify { spectrumCreator.create(frequencyBins, null) }
        assertEquals(spectrum, sut.spectrum)
    }

    @Test
    fun `setting a hovered bin updates the spectrum`() {
        val sut = createSUT()
        val hoveredBin = nextSpectrumBin()

        sut.setHoveredBin(hoveredBin)

        verify { spectrumCreator.create(listOf(), hoveredBin.frequency) }
        assertEquals(spectrum, sut.spectrum)
    }

    @Test
    fun `setting a hovered bin updates the hovered frequency`() {
        val sut = createSUT()
        val hoveredBin = SpectrumBin(20.123F, 1F, false)

        sut.setHoveredBin(hoveredBin)

        assertEquals("20.12 Hz", sut.hoveredFrequency)
    }

    @Test
    fun `clearing the hovered bin clears the hovered frequency`() {
        val sut = createSUT()
        sut.setHoveredBin(nextSpectrumBin())

        sut.setHoveredBin(null)

        assertEquals("", sut.hoveredFrequency)
    }

}


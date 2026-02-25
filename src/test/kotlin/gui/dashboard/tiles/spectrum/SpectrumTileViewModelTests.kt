package gui.dashboard.tiles.spectrum

import io.mockk.clearAllMocks
import lightOrgan.spectrum.SpectrumManagerFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextFrequencyBins
import kotlin.random.Random

class SpectrumTileViewModelTests {

    private lateinit var spectrumManager: SpectrumManagerFixture

    private val newFrequencyBins = nextFrequencyBins()

    @BeforeEach
    fun setupHappyPath() {
        spectrumManager = SpectrumManagerFixture.create()
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): SpectrumTileViewModel {
        return SpectrumTileViewModel(
            spectrumManager = spectrumManager.mock,
        )
    }

    @Test
    fun `when new frequency bins are available, then update the displayed spectrum`() {
        val sut = createSUT()

        spectrumManager.frequencyBins.value = newFrequencyBins

        assertEquals(newFrequencyBins, sut.frequencyBins.value)
    }

    @Test
    fun `highlight a frequency bin`() {
        val sut = createSUT()
        spectrumManager.frequencyBins.value = newFrequencyBins

        val index = Random.nextInt(newFrequencyBins.size)
        sut.highlightedIndex = index

        assertEquals(newFrequencyBins[index], sut.highlightedBin)
    }
    
}


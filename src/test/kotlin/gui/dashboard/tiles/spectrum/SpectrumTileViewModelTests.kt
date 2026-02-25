package gui.dashboard.tiles.spectrum

import dsp.fft.FrequencyBin
import io.mockk.clearAllMocks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import lightOrgan.spectrum.SpectrumManagerFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class SpectrumTileViewModelTests {

    private lateinit var spectrumManager: SpectrumManagerFixture
    private val spectrumMultiplier: Float = 2f
    private val sutScope = TestScope()
    private val sharingPolicy = SharingStarted.Eagerly


    private val newBin1 = FrequencyBin(frequency = 1f, magnitude = 1f)
    private val newBin2 = FrequencyBin(frequency = 2f, magnitude = 2f)
    private val newBin3 = FrequencyBin(frequency = 3f, magnitude = 3f)
    private val newFrequencyBins = listOf(newBin1, newBin2, newBin3)

    private val scaledBin1 = FrequencyBin(frequency = 1f, magnitude = 2f)
    private val scaledBin2 = FrequencyBin(frequency = 2f, magnitude = 4f)
    private val scaledBin3 = FrequencyBin(frequency = 3f, magnitude = 6f)
    private val scaledFrequencyBins = listOf(scaledBin1, scaledBin2, scaledBin3)

    @BeforeEach
    fun setupHappyPath() {
        spectrumManager = SpectrumManagerFixture.create()
    }

    @AfterEach
    fun tearDown() {
        sutScope.cancel()
        clearAllMocks()
    }

    private fun createSUT(): SpectrumTileViewModel {
        return SpectrumTileViewModel(
            spectrumManager = spectrumManager.mock,
            spectrumMultiplier = spectrumMultiplier,
            scope = sutScope,
            sharingPolicy = sharingPolicy
        )
    }

    @Test
    fun `when new frequency bins are available, then update the displayed spectrum`() = runTest {
        val sut = createSUT()

        spectrumManager.frequencyBins.value = newFrequencyBins
        sutScope.advanceUntilIdle()

        assertEquals(scaledFrequencyBins, sut.frequencyBins.value)
    }

    @Test
    fun `highlight a frequency bin`() = runTest {
        val sut = createSUT()
        spectrumManager.frequencyBins.value = newFrequencyBins
        sutScope.advanceUntilIdle()

        val index = Random.nextInt(newFrequencyBins.size)
        sut.highlightedIndex = index

        assertEquals(scaledFrequencyBins[index], sut.highlightedBin)
    }

}


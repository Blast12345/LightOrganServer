package gui.dashboard.tiles.spectrum

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
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
import toolkit.monkeyTest.nextFrequencyBin
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class SpectrumTileViewModelTests {

    private val config: SpectrumGuiConfig = mockk()
    private lateinit var spectrumManager: SpectrumManagerFixture
    private val sutScope = TestScope()
    private val sharingPolicy = SharingStarted.Eagerly

    private val allBins = (1..100).map { nextFrequencyBin(frequency = it.toFloat()) }

    @BeforeEach
    fun setupHappyPath() {
        every { config.lowestFrequency } returns 0f
        every { config.highestFrequency } returns Float.MAX_VALUE
        every { config.scale } returns 1f
        spectrumManager = SpectrumManagerFixture.create()
    }

    @AfterEach
    fun tearDown() {
        sutScope.cancel()
        clearAllMocks()
    }

    private fun createSUT(): SpectrumTileViewModel {
        return SpectrumTileViewModel(
            config = config,
            spectrumManager = spectrumManager.mock,
            scope = sutScope,
            sharingPolicy = sharingPolicy
        )
    }

    @Test
    fun `when new frequency bins are available, then update the displayed spectrum`() = runTest {
        val sut = createSUT()

        spectrumManager.frequencyBins.value = allBins
        sutScope.advanceUntilIdle()

        assertEquals(allBins, sut.displayedBins.value)
    }

    @Test
    fun `specify a frequency range`() {
        val sut = createSUT()
        sut.lowestFrequency = 10f
        sut.highestFrequency = 90f

        spectrumManager.frequencyBins.value = allBins
        sutScope.advanceUntilIdle()

        val binsInRange = allBins.filter { it.frequency in 10f..90f }
        assertEquals(binsInRange, sut.displayedBins.value)
    }

    @Test
    fun `scale the displayed spectrum`() {
        val sut = createSUT()
        sut.scale = 2f

        spectrumManager.frequencyBins.value = allBins
        sutScope.advanceUntilIdle()

        val scaledBins = allBins.map { it.copy(magnitude = it.magnitude * 2f) }
        assertEquals(scaledBins, sut.displayedBins.value)
    }

    @Test
    fun `highlight a frequency bin`() {
        val sut = createSUT()
        spectrumManager.frequencyBins.value = allBins
        sutScope.advanceUntilIdle()

        val index = Random.nextInt(sut.displayedBins.value.size)
        sut.highlightedIndex = index

        assertEquals(sut.displayedBins.value[index], sut.highlightedBin)
    }

}


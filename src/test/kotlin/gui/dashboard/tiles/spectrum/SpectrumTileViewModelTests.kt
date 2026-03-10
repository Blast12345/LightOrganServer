package gui.dashboard.tiles.spectrum

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
import toolkit.monkeyTest.nextFrequencyBin
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class SpectrumTileViewModelTests {

    private lateinit var spectrumManager: SpectrumManagerFixture
    private val sutScope = TestScope()
    private val sharingPolicy = SharingStarted.Eagerly

    private val allBins = (1..100).map { nextFrequencyBin(frequency = it.toFloat()) }

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

    // Filtering
    @Test
    fun `given a lowest frequency, then displayed bins are above that frequency`() = runTest {
        val sut = createSUT()
        spectrumManager.frequencyBins.value = allBins

        sut.lowestFrequency = 10f
        sutScope.advanceUntilIdle()

        val expected = allBins.filter { it.frequency >= 10f }
        assertEquals(expected, sut.displayedBins.value)
    }

    @Test
    fun `given a highest frequency, then displayed bins are below that frequency`() = runTest {
        val sut = createSUT()
        spectrumManager.frequencyBins.value = allBins

        sut.highestFrequency = 90f
        sutScope.advanceUntilIdle()

        val expected = allBins.filter { it.frequency <= 90f }
        assertEquals(expected, sut.displayedBins.value)
    }

    @Test
    fun `given a lowest and highest frequency, then displayed bins are between those frequencies`() = runTest {
        val sut = createSUT()
        spectrumManager.frequencyBins.value = allBins

        sut.lowestFrequency = 10f
        sut.highestFrequency = 90f
        sutScope.advanceUntilIdle()

        val expected = allBins.filter { it.frequency in 10f..90f }
        assertEquals(expected, sut.displayedBins.value)
    }

    // Highlighting
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


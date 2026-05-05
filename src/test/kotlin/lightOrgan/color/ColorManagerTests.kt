package lightOrgan.color

import androidx.compose.ui.graphics.Color
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextComposeColor
import toolkit.monkeyTest.nextFrequencyBins

@OptIn(ExperimentalCoroutinesApi::class)
class ColorManagerTests {

    private val peakFrequencyBinsFinder: PeakFrequencyBinsFinder = mockk()
    private val colorCalculator: ColorCalculator = mockk()

    private val frequencyBins = nextFrequencyBins()
    private val peakFrequencyBins = nextFrequencyBins()
    private val color = nextComposeColor()

    @BeforeEach
    fun setupHappyPath() {
        every { peakFrequencyBinsFinder.find(frequencyBins) } returns peakFrequencyBins
        every { colorCalculator.calculate(peakFrequencyBins) } returns color
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): ColorManager {
        return ColorManager(
            peakFrequencyBinsFinder = peakFrequencyBinsFinder,
            colorCalculator = colorCalculator,
        )
    }

    @Test
    fun `calculate a color from frequency bins`() {
        val sut = createSUT()

        val actual = sut.calculate(frequencyBins)

        assertEquals(color, actual)
        assertEquals(color, sut.color.value)
    }

    @Test
    fun `given no peaks are present, then the color is black`() {
        val sut = createSUT()

        every { peakFrequencyBinsFinder.find(frequencyBins) } returns listOf()
        val actual = sut.calculate(frequencyBins)

        assertEquals(Color.Black, actual)
        assertEquals(Color.Black, sut.color.value)
    }

}
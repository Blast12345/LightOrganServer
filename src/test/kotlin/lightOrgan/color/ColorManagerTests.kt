package lightOrgan.color

import androidx.compose.ui.graphics.Color
import dsp.peakExtraction.SpectralPeakExtractor
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
import toolkit.monkeyTest.nextSpectralPeaks

@OptIn(ExperimentalCoroutinesApi::class)
class ColorManagerTests {

    private val peakExtractor: SpectralPeakExtractor = mockk()
    private val colorCalculator: ColorCalculator = mockk()

    private val spectrum = nextFrequencyBins()
    private val spectralPeaks = nextSpectralPeaks()
    private val color = nextComposeColor()

    @BeforeEach
    fun setupHappyPath() {
        every { peakExtractor.extract(spectrum) } returns spectralPeaks
        every { colorCalculator.calculate(spectralPeaks) } returns color
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): ColorManager {
        return ColorManager(
            peakExtractor = peakExtractor,
            colorCalculator = colorCalculator,
        )
    }

    @Test
    fun `calculate a color from frequency bins`() {
        val sut = createSUT()

        val actual = sut.calculate(spectrum)

        assertEquals(color, actual)
        assertEquals(color, sut.color.value)
    }

    @Test
    fun `given no peaks are present, then the color is black`() {
        val sut = createSUT()

        every { peakExtractor.extract(spectrum) } returns listOf()
        val actual = sut.calculate(spectrum)

        assertEquals(Color.Black, actual)
        assertEquals(Color.Black, sut.color.value)
    }

}
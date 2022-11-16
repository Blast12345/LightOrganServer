package colorService

import colorService.color.FakeColorGenerator
import colorService.sound.FakeFrequencyBinsService
import colorService.sound.FrequencyBin
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.awt.Color

class ColorServiceTests {

    private lateinit var frequencyBinsService: FakeFrequencyBinsService
    private lateinit var colorGenerator: FakeColorGenerator
    private val frequencyBins = listOf(FrequencyBin(100.0, 10.0))

    @Before
    fun setup() {
        frequencyBinsService = FakeFrequencyBinsService()
        colorGenerator = FakeColorGenerator()
    }

    private fun createSUT(): ColorService {
        return ColorService(frequencyBinsService, colorGenerator)
    }

    @Test
    fun `a color is generated when frequency bins become available`() {
        val sut = createSUT()

        var nextColor: Color? = null
        sut.listenForNextColor {
            nextColor = it
        }

        frequencyBinsService.lambda?.invoke(frequencyBins)

        assertEquals(colorGenerator.frequencyBins, frequencyBins)
        assertEquals(nextColor, colorGenerator.color)
    }

}
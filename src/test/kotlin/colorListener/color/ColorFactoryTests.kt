package colorListener.color

import colorListener.sound.FrequencyBin
import extensions.getHue
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.awt.Color

class ColorFactoryTests {

    private lateinit var hueFactory: FakeHueFactory

    @Before
    fun setup() {
        hueFactory = FakeHueFactory()
    }

    private fun createSUT(): ColorFactory {
        return ColorFactory(hueFactory)
    }

    @Test
    fun `the color is black when there are no frequency bins`() {
        val sut = createSUT()
        val actualColor = sut.colorFrom(emptyList())
        assertEquals(Color.black, actualColor)
    }

    @Test
    fun `the colors hue is derived from the frequency bins`() {
        val sut = createSUT()
        val frequencyBins = listOf(FrequencyBin(1.0, 1.0))
        val actualColor = sut.colorFrom(frequencyBins)
        assertEquals(frequencyBins, hueFactory.frequencyBins)
        assertEquals(hueFactory.hue, actualColor.getHue(), 0.001f)
    }

}
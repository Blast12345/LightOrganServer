package colorListener.color

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import sound.frequencyBins.FrequencyBin
import toolkit.getHue
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
    fun `the colors hue is derived from the frequency bins`() {
        val sut = createSUT()
        val frequencyBins = listOf(FrequencyBin(1.0, 1.0))
        val color = sut.colorFrom(frequencyBins)
        assertEquals(hueFactory.hue!!, color.getHue(), 0.001f)
        assertEquals(frequencyBins, hueFactory.frequencyBins)
    }

    @Test
    fun `the color is black when there is no hue`() {
        val sut = createSUT()
        hueFactory.hue = null
        val color = sut.colorFrom(emptyList())
        assertEquals(Color.black, color)
    }

}
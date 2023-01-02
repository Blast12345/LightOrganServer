package color

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.frequencyBins.FakeFrequencyBinsFactory
import sound.input.samples.NormalizedAudioFrame
import toolkit.getHue
import java.awt.Color

class ColorFactoryTests {

    private lateinit var frequencyBinsFactory: FakeFrequencyBinsFactory
    private lateinit var hueFactory: FakeHueFactory
    private val samples = doubleArrayOf(1.1)
    private val audioFrame = NormalizedAudioFrame(samples)

    @BeforeEach
    fun setup() {
        frequencyBinsFactory = FakeFrequencyBinsFactory()
        hueFactory = FakeHueFactory()
    }

    private fun createSUT(): ColorFactory {
        return ColorFactory(frequencyBinsFactory, hueFactory)
    }

    @Test
    fun `the colors hue is derived from the sample`() {
        val sut = createSUT()
        val color = sut.colorFor(audioFrame)
        assertEquals(hueFactory.hue!!, color.getHue(), 0.001f)
        assertEquals(frequencyBinsFactory.frequencyBins, hueFactory.frequencyBins)
        assertEquals(audioFrame, frequencyBinsFactory.audioFrame)
    }

    @Test
    fun `the color is black when there is no hue`() {
        val sut = createSUT()
        hueFactory.hue = null
        val color = sut.colorFor(audioFrame)
        assertEquals(Color.black, color)
    }

    @Test
    fun `the lowest supported frequency is 20hz`() {
        val sut = createSUT()
        sut.colorFor(audioFrame)
        assertEquals(20F, frequencyBinsFactory.lowestSupportedFrequency)
    }

}
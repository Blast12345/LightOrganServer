package color

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import sound.frequencyBins.FakeFrequencyBinsFactory
import sound.input.sample.AudioFrame
import toolkit.getHue
import java.awt.Color
import javax.sound.sampled.AudioFormat

class ColorFactoryTests {

    private lateinit var frequencyBinsFactory: FakeFrequencyBinsFactory
    private lateinit var hueFactory: FakeHueFactory
    private val frame = byteArrayOf(1)
    private val format = AudioFormat(44100F, 8, 1, true, true)
    private val audioFrame = AudioFrame(frame, format)

    @Before
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
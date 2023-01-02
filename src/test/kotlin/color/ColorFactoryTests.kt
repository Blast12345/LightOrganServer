package color

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinsFactory
import sound.input.samples.NormalizedAudioFrame
import toolkit.getHue
import java.awt.Color

class ColorFactoryTests {

    @MockK
    private lateinit var frequencyBinsFactory: FrequencyBinsFactory

    @MockK
    private lateinit var hueFactory: HueFactory

    @MockK
    private lateinit var audioFrame: NormalizedAudioFrame

    @BeforeEach
    fun setup() {
        frequencyBinsFactory = mockk()
        hueFactory = mockk()
        audioFrame = mockk()

        every { hueFactory.createFrom(any()) } returns 0F
        every { frequencyBinsFactory.createFrom(any(), any()) } returns listOf()
        every { audioFrame.samples } returns doubleArrayOf()
    }

    private fun createSUT(): ColorFactory {
        return ColorFactory(frequencyBinsFactory, hueFactory)
    }

    @Test
    fun `create a color`() {
        val sut = createSUT()
        val actual = sut.createFor(audioFrame)
        assertTrue(actual is Color)
    }

    @Test
    fun `the hue is derived from the frequencies`() {
        val sut = createSUT()

        val frequencyBins = listOf(FrequencyBin(100.0, 1.0))
        every { frequencyBinsFactory.createFrom(audioFrame, any()) } returns frequencyBins

        val hue = 0.123f
        every { hueFactory.createFrom(frequencyBins) } returns hue

        val actual = sut.createFor(audioFrame)
        assertEquals(hue, actual.getHue(), 0.001f)
    }

    @Test
    fun `the color is black when there is no hue`() {
        val sut = createSUT()
        every { hueFactory.createFrom(any()) } returns null
        val actual = sut.createFor(audioFrame)
        assertEquals(Color.black, actual)
    }

    @Test
    fun `the lowest supported frequency is 20hz`() {
        val sut = createSUT()
        sut.createFor(audioFrame)
        verify { frequencyBinsFactory.createFrom(audioFrame, 20F) }
    }

}
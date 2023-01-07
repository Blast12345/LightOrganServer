package color

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.frequencyBins.FrequencyBinsFactory
import sound.frequencyBins.FrequencyBinsFactoryInterface
import toolkit.getHue
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextFrequencyBins
import java.awt.Color
import kotlin.random.Random

class ColorFactoryTests {

    private var frequencyBinsFactory: FrequencyBinsFactoryInterface = mockk()
    private var hueFactory: HueFactoryInterface = mockk()

    private val audioFrame = nextAudioFrame()
    private val frequencyBins = nextFrequencyBins()

    @BeforeEach
    fun setup() {
        every { frequencyBinsFactory.create(audioFrame, any()) } returns frequencyBins
        every { hueFactory.create(any()) } returns Random.nextFloat()
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): ColorFactory {
        return ColorFactory(
            frequencyBinsFactory = frequencyBinsFactory,
            hueFactory = hueFactory
        )
    }

    @Test
    fun `the colors hue is computed from frequency bins`() {
        val sut = createSUT()
        val hue = Random.nextFloat()
        every { hueFactory.create(frequencyBins) } returns hue
        val actual = sut.create(audioFrame)
        assertEquals(hue, actual.getHue(), 0.001F)
    }

    @Test
    fun `the color is black when there is no hue`() {
        val sut = createSUT()
        every { hueFactory.create(any()) } returns null
        val actual = sut.create(audioFrame)
        assertEquals(Color.black, actual)
    }

}
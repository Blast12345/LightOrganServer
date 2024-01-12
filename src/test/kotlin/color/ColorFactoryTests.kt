package color

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.bins.frequency.BassBinsFactory
import toolkit.monkeyTest.nextAudioFrame
import toolkit.monkeyTest.nextFrequencyBinList
import wrappers.color.Color
import kotlin.random.Random

class ColorFactoryTests {

    private val audioFrame = nextAudioFrame()

    private val bassBinsFactory: BassBinsFactory = mockk()
    private val bassBins = nextFrequencyBinList()
    private val hueCalculator: HueCalculator = mockk()
    private val hue = Random.nextFloat()
    private val brightnessCalculator: BrightnessCalculator = mockk()
    private val brightness = Random.nextFloat()

    @BeforeEach
    fun setupHappyPath() {
        every { bassBinsFactory.create(audioFrame) } returns bassBins
        every { hueCalculator.calculate(bassBins) } returns hue
        every { brightnessCalculator.calculate(bassBins) } returns brightness
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): ColorFactory {
        return ColorFactory(
            bassBinsFactory = bassBinsFactory,
            hueCalculator = hueCalculator,
            brightnessCalculator = brightnessCalculator
        )
    }

    @Test
    fun `get the color for an audio frame`() {
        val sut = createSUT()

        val color = sut.create(audioFrame)

        assertEquals(hue, color.hue, 0.01F)
        assertEquals(1F, color.saturation, 0.01F)
        assertEquals(brightness, color.brightness, 0.01F)
    }

    @Test
    fun `the color is black when the hue cannot be determined`() {
        val sut = createSUT()
        every { hueCalculator.calculate(bassBins) } returns null

        val actual = sut.create(audioFrame)

        assertEquals(Color.black, actual)
    }

    @Test
    fun `the color is black when the brightness cannot be determined`() {
        val sut = createSUT()
        every { brightnessCalculator.calculate(bassBins) } returns null

        val actual = sut.create(audioFrame)

        assertEquals(Color.black, actual)
    }

}

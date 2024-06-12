package color

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFrame
import wrappers.color.Color
import kotlin.random.Random

class ColorFactoryTests {

    private val audioFrame = nextAudioFrame()

    private val hueCalculator: HueCalculator = mockk()
    private val hue = Random.nextFloat()
    private val brightnessCalculator: BrightnessCalculator = mockk()
    private val brightness = Random.nextFloat()

    @BeforeEach
    fun setupHappyPath() {
        every { hueCalculator.calculate(audioFrame) } returns hue
        every { brightnessCalculator.calculate(audioFrame) } returns brightness
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): ColorFactory {
        return ColorFactory(
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
        every { hueCalculator.calculate(audioFrame) } returns null

        val actual = sut.create(audioFrame)

        assertEquals(Color.black, actual)
    }

    @Test
    fun `the color is black when the brightness cannot be determined`() {
        val sut = createSUT()
        every { brightnessCalculator.calculate(audioFrame) } returns null

        val actual = sut.create(audioFrame)

        assertEquals(Color.black, actual)
    }

}

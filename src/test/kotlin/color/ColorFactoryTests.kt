package color

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextFrequencyBins
import wrappers.color.Color
import kotlin.random.Random

class ColorFactoryTests {

    private val frequencyBins = nextFrequencyBins()
    private val hueCalculator: HueCalculator = mockk()
    private val hue = Random.nextFloat()
    private val brightnessCalculator: BrightnessCalculator = mockk()
    private val brightness = Random.nextFloat()

    @BeforeEach
    fun setupHappyPath() {
        every { hueCalculator.calculate(frequencyBins) } returns hue
        every { brightnessCalculator.calculate(frequencyBins) } returns brightness
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

        val color = sut.create(frequencyBins)

        assertEquals(hue, color.hue, 0.1F)
        assertEquals(1F, color.saturation, 0.01F)
        assertEquals(brightness, color.brightness, 0.01F)
    }

    @Test
    fun `the color is black when the hue cannot be determined`() {
        val sut = createSUT()
        every { hueCalculator.calculate(frequencyBins) } returns null

        val actual = sut.create(frequencyBins)

        assertEquals(Color.black, actual)
    }

    @Test
    fun `the color is black when the brightness cannot be determined`() {
        val sut = createSUT()
        every { brightnessCalculator.calculate(frequencyBins) } returns null

        val actual = sut.create(frequencyBins)

        assertEquals(Color.black, actual)
    }

}

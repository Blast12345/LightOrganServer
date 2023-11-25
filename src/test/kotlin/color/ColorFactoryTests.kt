package color

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextFrequencyBin
import wrappers.color.Color
import kotlin.random.Random

class ColorFactoryTests {

    private val frequencyBin = nextFrequencyBin()
    private val hueCalculator: HueCalculator = mockk()
    private val hue = Random.nextFloat()
    private val brightnessFactory: BrightnessFactory = mockk(relaxed = true)
    private val brightness = Random.nextFloat()

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): ColorFactory {
        return ColorFactory(
            hueCalculator = hueCalculator,
            brightnessFactory = brightnessFactory
        )
    }

    @Test
    fun `the color is created from the frequency bin`() {
        val sut = createSUT()
        every { hueCalculator.calculate(frequencyBin.frequency) } returns hue
        every { brightnessFactory.create(frequencyBin.magnitude) } returns brightness

        val color = sut.create(frequencyBin)

        assertEquals(hue, color.hue, 0.01F)
        assertEquals(1F, color.saturation, 0.01F)
        assertEquals(brightness, color.brightness, 0.01F)
    }

    @Test
    fun `the color is black when there is no frequency bin`() {
        val sut = createSUT()

        val actual = sut.create(null)

        val black = Color(0F, 0F, 0F)
        assertEquals(black, actual)
    }
}
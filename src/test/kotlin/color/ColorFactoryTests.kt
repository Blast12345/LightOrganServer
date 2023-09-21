package color

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextFrequencyBin
import wrappers.color.Color
import kotlin.random.Random

class ColorFactoryTests {

    private val hueFactory: HueFactory = mockk()
    private val brightnessFactory: BrightnessFactory = mockk()
    private val frequencyBin = nextFrequencyBin()

    @BeforeEach
    fun setup() {
        every { hueFactory.create(any()) } returns Random.nextFloat()
        every { brightnessFactory.create(any()) } returns Random.nextFloat()
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): ColorFactory {
        return ColorFactory(
            hueFactory = hueFactory,
            brightnessFactory = brightnessFactory
        )
    }

    @Test
    fun `the hue is corresponds to the frequency`() {
        val sut = createSUT()
        val hue = Random.nextFloat()
        every { hueFactory.create(frequencyBin.frequency) } returns hue

        val color = sut.create(frequencyBin)

        assertEquals(hue, color.hue, 0.01F)
    }

    @Test
    fun `the color is fully saturated`() {
        val sut = createSUT()

        val color = sut.create(frequencyBin)

        assertEquals(1F, color.saturation, 0.01F)
    }

    @Test
    fun `the brightness is corresponds to the frequency's magnitude`() {
        val sut = createSUT()
        val brightness = Random.nextFloat()
        every { brightnessFactory.create(frequencyBin) } returns brightness

        val color = sut.create(frequencyBin)

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
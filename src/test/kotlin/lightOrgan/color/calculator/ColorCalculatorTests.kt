package lightOrgan.color.calculator

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import lightOrgan.color.calculator.hue.OctaveHueCalculator
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextFrequencyBins
import kotlin.random.Random

class ColorCalculatorTests {

    private val hueCalculator: OctaveHueCalculator = mockk()
    private val brightnessCalculator: BrightnessCalculator = mockk()

    private val frequencyBins = nextFrequencyBins()
    private val hue: Float = Random.nextFloat()
    private val brightness: Float = Random.nextFloat()

    @BeforeEach
    fun setupHappyPath() {
        every { hueCalculator.calculate(frequencyBins) } returns hue
        every { brightnessCalculator.calculate(frequencyBins) } returns brightness
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): ColorCalculator {
        return ColorCalculator(
            hueCalculator = hueCalculator,
            brightnessCalculator = brightnessCalculator
        )
    }

    @Test
    fun `calculate a color from frequency bins`() {
        val sut = createSUT()

        val actual = sut.calculate(frequencyBins)!!

        assertEquals(hue, actual.hue, 0.01f)
        assertEquals(brightness, actual.brightness, 0.01f)
        assertEquals(1F, actual.saturation, 0.01f)
    }

    @Test
    fun `when the hue cannot be determined, return null`() {
        val sut = createSUT()
        every { hueCalculator.calculate(frequencyBins) } returns null

        val actual = sut.calculate(frequencyBins)

        assertEquals(null, actual)
    }

    @Test
    fun `when the brightness cannot be determined, return null`() {
        val sut = createSUT()
        every { brightnessCalculator.calculate(frequencyBins) } returns null

        val actual = sut.calculate(frequencyBins)

        assertEquals(null, actual)
    }

}
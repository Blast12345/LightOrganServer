package color

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.getHue
import toolkit.monkeyTest.nextFrequencyBins
import java.awt.Color
import kotlin.random.Random

class ColorFactoryTests {

    private lateinit var hueFactory: HueFactory
    private val frequencyBins = nextFrequencyBins()

    @BeforeEach
    fun setup() {
        hueFactory = mockk()
        every { hueFactory.create(any()) } returns Random.nextFloat()
    }

    private fun createSUT(): ColorFactory {
        return ColorFactory(hueFactory)
    }

    @Test
    fun `create a color`() {
        val sut = createSUT()
        val actual = sut.create(frequencyBins)
        assertTrue(actual is Color)
    }

    @Test
    fun `the hue is computed from the frequency bins`() {
        val sut = createSUT()
        val hue = Random.nextFloat()
        every { hueFactory.create(frequencyBins) } returns hue
        val actual = sut.create(frequencyBins)
        assertEquals(hue, actual.getHue(), 0.001F)
    }

    @Test
    fun `the color is black when there is no hue`() {
        val sut = createSUT()
        every { hueFactory.create(any()) } returns null
        val actual = sut.create(frequencyBins)
        assertEquals(Color.black, actual)
    }

}
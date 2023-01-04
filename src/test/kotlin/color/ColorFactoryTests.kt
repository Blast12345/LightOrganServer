package color

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.getHue
import toolkit.monkeyTest.nextFrequencyBins
import java.awt.Color
import kotlin.random.Random

class ColorFactoryTests {

    private var hueFactory: HueFactory = mockk()
    private val frequencyBins = nextFrequencyBins()

    @BeforeEach
    fun setup() {
        every { hueFactory.create(any()) } returns Random.nextFloat()
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): ColorFactory {
        return ColorFactory(hueFactory)
    }

    @Test
    fun `the colors hue is computed from the frequency bins`() {
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
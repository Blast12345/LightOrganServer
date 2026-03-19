package color

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sound.bins.frequency.GreatestMagnitudeFinder
import toolkit.monkeyTest.nextFrequencyBins
import kotlin.random.Random

class BrightnessCalculatorTests {

    private val frequencyBins = nextFrequencyBins()

    private val greatestMagnitudeFinder: GreatestMagnitudeFinder = mockk()
    private val greatestMagnitude = Random.nextFloat()

    private fun createSUT(): BrightnessCalculator {
        return BrightnessCalculator(
            multiplier = 1f,
            greatestMagnitudeFinder = greatestMagnitudeFinder
        )
    }

    @BeforeEach
    fun setupHappyPath() {
        every { greatestMagnitudeFinder.find(frequencyBins) } returns greatestMagnitude
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `get the brightness for a list of frequency bins`() {
        val sut = createSUT()

        val brightness = sut.calculate(frequencyBins)

        assertEquals(greatestMagnitude, brightness)
    }

    @Test
    fun `given the greatest magnitude is greater than 1, then return 1`() {
        val sut = createSUT()
        every { greatestMagnitudeFinder.find(any()) } returns 1.5F

        val brightness = sut.calculate(frequencyBins)

        assertEquals(1F, brightness)
    }

    @Test
    fun `given there is no greatest magnitude, then return null`() {
        val sut = createSUT()
        every { greatestMagnitudeFinder.find(any()) } returns null

        val brightness = sut.calculate(frequencyBins)

        assertEquals(null, brightness)
    }

}

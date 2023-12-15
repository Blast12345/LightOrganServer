package color.brightness

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextFrequencyBinList

class BrightnessCalculatorTests {

    private val greatestMagnitudeCalculator: GreatestMagnitudeCalculator = mockk()
    private val frequencyBins = nextFrequencyBinList()

    private fun createSUT(): BrightnessCalculator {
        return BrightnessCalculator(
            greatestMagnitudeCalculator = greatestMagnitudeCalculator
        )
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    @Test
    fun `given the greatest magnitude is less than 1, then return the greatest magnitude`() {
        val sut = createSUT()
        every { greatestMagnitudeCalculator.calculate(frequencyBins) } returns 0.5F

        val brightness = sut.calculate(frequencyBins)

        assertEquals(0.5F, brightness)
    }

    @Test
    fun `given the greatest magnitude is greater than 1, then return 1`() {
        val sut = createSUT()
        every { greatestMagnitudeCalculator.calculate(frequencyBins) } returns 1.5F

        val brightness = sut.calculate(frequencyBins)

        assertEquals(1F, brightness)
    }

    @Test
    fun `given there is no greatest magnitude, then return null`() {
        val sut = createSUT()
        every { greatestMagnitudeCalculator.calculate(frequencyBins) } returns null

        val brightness = sut.calculate(frequencyBins)

        assertEquals(null, brightness)
    }

}
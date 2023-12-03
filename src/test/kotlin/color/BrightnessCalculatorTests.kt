package color

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextFrequencyBin
import kotlin.random.Random

class BrightnessCalculatorTests {

    private fun createSUT(): BrightnessCalculator {
        return BrightnessCalculator()
    }

    @Test
    fun `given the greatest magnitude is less than 1, then return the greatest magnitude`() {
        val sut = createSUT()
        val greatestMagnitude = Random.nextFloat()

        val brightness = sut.calculate(
            listOf(
                nextFrequencyBin(magnitude = 0.0F),
                nextFrequencyBin(magnitude = greatestMagnitude),
            )
        )

        assertEquals(greatestMagnitude, brightness)
    }

    @Test
    fun `given the greatest magnitude is greater than or equal to 1, then return 1`() {
        val sut = createSUT()
        val greatestMagnitude = Random.nextFloat() + 1

        val brightness = sut.calculate(
            listOf(
                nextFrequencyBin(magnitude = 0.0F),
                nextFrequencyBin(magnitude = greatestMagnitude),
            )
        )

        assertEquals(1F, brightness)
    }

    @Test
    fun `given there are no frequency bins, then return null`() {
        val sut = createSUT()

        val brightness = sut.calculate(listOf())

        assertEquals(null, brightness)
    }

}
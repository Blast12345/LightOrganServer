package sound.octave

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OctaveWeightedAverageCalculatorTests {

    private fun createSUT(): OctaveWeightedAverageCalculator {
        return OctaveWeightedAverageCalculator()
    }

    @Test
    fun `calculate the weighted average`() {
        val sut = createSUT()

        val actual = sut.calculate(
            listOf(
                OctaveBin(0.0F, 98F),
                OctaveBin(0.5F, 1F),
                OctaveBin(1.0F, 1F)
            )
        )

        assertEquals(0.015F, actual)
    }

    @Test
    fun `when there are no bins, then null is returned`() {
        val sut = createSUT()

        val actual = sut.calculate(listOf())

        assertEquals(null, actual)
    }

    @Test
    fun `when the total magnitude is zero, then null is returned`() {
        val sut = createSUT()

        val actual = sut.calculate(
            listOf(
                OctaveBin(0.0F, 0F),
                OctaveBin(0.5F, 0F),
                OctaveBin(1.0F, 0F)
            )
        )

        assertEquals(null, actual)
    }

}
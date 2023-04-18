package gui.dashboard.tiles.statistics

import gui.dashboard.tiles.Statistics.LowestDiscernibleFrequencyCalculator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LowestDiscernibleFrequencyCalculatorTests {

    @Test
    fun `calculate the lowest discernible frequency`() {
        val sut = LowestDiscernibleFrequencyCalculator()

        val actual = sut.calculate(0.05f)

        assertEquals(20f, actual, 0.001f)
    }

}
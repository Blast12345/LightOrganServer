package gui.dashboard.tiles.statistics

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrequencyResolutionCalculatorTests {

    @Test
    fun `calculate the frequency resolution`() {
        val sut = FrequencyResolutionCalculator()

        val actual = sut.calculate(
            sampleRate = 16f,
            sampleSize = 8,
            numberOfChannels = 2
        )

        assertEquals(4f, actual, 0.001f)
    }

}
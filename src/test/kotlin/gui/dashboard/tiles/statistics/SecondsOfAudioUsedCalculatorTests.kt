package gui.dashboard.tiles.statistics

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SecondsOfAudioUsedCalculatorTests {

    @Test
    fun `calculate the seconds of audio used`() {
        val sut = SecondsOfAudioUsedCalculator()

        val actual = sut.calculate(
            sampleSize = 8,
            sampleRate = 16f,
            numberOfChannels = 2
        )

        assertEquals(0.25f, actual, 0.001f)
    }

}
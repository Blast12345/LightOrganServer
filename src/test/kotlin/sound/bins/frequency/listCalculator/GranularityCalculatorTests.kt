package sound.bins.frequency.listCalculator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAudioFormat

class GranularityCalculatorTests {

    private fun createSUT(): GranularityCalculator {
        return GranularityCalculator()
    }

    @Test
    fun `calculate the frequency granularity given some number of magnitudes and an audio format`() {
        val sut = createSUT()
        val numberOfBins = 50
        val audioFormat = nextAudioFormat(sampleRate = 200f, channels = 2)

        val granularity = sut.calculate(numberOfBins, audioFormat)

        assertEquals(4F, granularity, 0.001F)
    }

}

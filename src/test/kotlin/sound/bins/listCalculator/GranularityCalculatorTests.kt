package sound.bins.listCalculator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.bins.frequencyBins.listCalculator.GranularityCalculator
import toolkit.monkeyTest.nextAudioFormatWrapper

class GranularityCalculatorTests {

    private fun createSUT(): GranularityCalculator {
        return GranularityCalculator()
    }

    @Test
    fun `calculate the frequency granularity given some number of magnitudes and an audio format`() {
        val sut = createSUT()
        val numberOfBins = 50
        val audioFormat = nextAudioFormatWrapper(nyquistFrequency = 100F, numberOfChannels = 2)

        val granularity = sut.calculate(numberOfBins, audioFormat)

        assertEquals(4F, granularity, 0.001F)
    }

}
package sound.bins.octave

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.bins.FrequencyBinsToOctaveBinsConverter
import sound.bins.frequency.dominant.frequency.PeakFrequencyBinsFinder
import toolkit.monkeyTest.nextFrequencyBins
import toolkit.monkeyTest.nextOctaveBins

class PeakOctaveBinsFinderTests {

    private val frequencyBins = nextFrequencyBins()

    private val peakFrequencyBinsFinder: PeakFrequencyBinsFinder = mockk()
    private val peakFrequencyBins = nextFrequencyBins()
    private val frequencyBinsToOctaveBinsConverter: FrequencyBinsToOctaveBinsConverter = mockk()
    private val convertedOctaveBins = nextOctaveBins()

    private fun createSUT(): PeakOctaveBinsFinder {
        return PeakOctaveBinsFinder(
            peakFrequencyBinsFinder = peakFrequencyBinsFinder,
            frequencyBinsToOctaveBinsConverter = frequencyBinsToOctaveBinsConverter
        )
    }

    @Test
    fun `given some frequency bins, find the peak octave positions`() {
        val sut = createSUT()
        every { peakFrequencyBinsFinder.find(frequencyBins) } returns peakFrequencyBins
        every { frequencyBinsToOctaveBinsConverter.convert(peakFrequencyBins) } returns convertedOctaveBins

        val actual = sut.find(frequencyBins)

        assertEquals(convertedOctaveBins, actual)
    }

}

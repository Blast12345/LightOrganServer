package sound.octave

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sound.bins.FrequencyToOctaveConverter
import sound.frequencyBins.dominant.frequency.PeakFrequencyBinsFinder
import toolkit.monkeyTest.nextFrequencyBinList
import toolkit.monkeyTest.nextOctaveBinList

class PeakOctaveBinsFinderTests {

    private val frequencyBins = nextFrequencyBinList()

    private val peakFrequencyBinsFinder: PeakFrequencyBinsFinder = mockk()
    private val peakFrequencyBins = nextFrequencyBinList()
    private val frequencyToOctaveConverter: FrequencyToOctaveConverter = mockk()
    private val convertedOctaveBins = nextOctaveBinList()

    private fun createSUT(): PeakOctaveBinsFinder {
        return PeakOctaveBinsFinder(
            peakFrequencyBinsFinder = peakFrequencyBinsFinder,
            frequencyToOctaveConverter = frequencyToOctaveConverter
        )
    }

    @Test
    fun `given some frequency bins, find the peak octave positions`() {
        val sut = createSUT()
        every { peakFrequencyBinsFinder.find(frequencyBins) } returns peakFrequencyBins
        every { frequencyToOctaveConverter.convert(peakFrequencyBins) } returns convertedOctaveBins
        
        val actual = sut.find(frequencyBins)

        assertEquals(convertedOctaveBins, actual)
    }

}
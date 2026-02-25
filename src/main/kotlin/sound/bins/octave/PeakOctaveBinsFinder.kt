package sound.bins.octave

import dsp.fft.FrequencyBins
import sound.bins.FrequencyBinsToOctaveBinsConverter
import sound.bins.frequency.dominant.frequency.PeakFrequencyBinsFinder

class PeakOctaveBinsFinder(
    private val peakFrequencyBinsFinder: PeakFrequencyBinsFinder = PeakFrequencyBinsFinder(),
    private val frequencyBinsToOctaveBinsConverter: FrequencyBinsToOctaveBinsConverter = FrequencyBinsToOctaveBinsConverter()
) {

    fun find(frequencyBins: FrequencyBins): OctaveBins {
        val peakBins = peakFrequencyBinsFinder.find(frequencyBins)
        return frequencyBinsToOctaveBinsConverter.convert(peakBins)
    }

}

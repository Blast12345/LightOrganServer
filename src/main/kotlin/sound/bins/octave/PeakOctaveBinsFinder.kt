package sound.bins.octave

import sound.bins.FrequencyBinListToOctaveBinListConverter
import sound.bins.frequency.FrequencyBinList
import sound.bins.frequency.dominant.frequency.PeakFrequencyBinsFinder

class PeakOctaveBinsFinder(
    private val peakFrequencyBinsFinder: PeakFrequencyBinsFinder = PeakFrequencyBinsFinder(),
    private val frequencyBinListToOctaveBinListConverter: FrequencyBinListToOctaveBinListConverter = FrequencyBinListToOctaveBinListConverter()
) {

    fun find(frequencyBins: FrequencyBinList): OctaveBinList {
        val peakBins = peakFrequencyBinsFinder.find(frequencyBins)
        return frequencyBinListToOctaveBinListConverter.convert(peakBins)
    }

}
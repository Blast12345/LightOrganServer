package sound.octave

import sound.bins.FrequencyToOctaveConverter
import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.dominant.frequency.PeakFrequencyBinsFinder

class PeakOctaveBinsFinder(
    private val peakFrequencyBinsFinder: PeakFrequencyBinsFinder = PeakFrequencyBinsFinder(),
    private val frequencyToOctaveConverter: FrequencyToOctaveConverter = FrequencyToOctaveConverter()
) {

    fun find(frequencyBins: FrequencyBinList): OctaveBinList {
        val peakBins = peakFrequencyBinsFinder.find(frequencyBins)
        return frequencyToOctaveConverter.convert(peakBins)
    }

}
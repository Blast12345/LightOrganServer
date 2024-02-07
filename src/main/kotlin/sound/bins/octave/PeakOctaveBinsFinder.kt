package sound.bins.octave

import sound.bins.FrequencyBinsToOctaveBinsConverter
import sound.bins.frequency.FrequencyBins
import sound.bins.frequency.dominant.frequency.PeakFrequencyBinsFinder

class PeakOctaveBinsFinder(
    private val peakFrequencyBinsFinder: PeakFrequencyBinsFinder = PeakFrequencyBinsFinder(),
    private val frequencyBinsToOctaveBinsConverter: FrequencyBinsToOctaveBinsConverter = FrequencyBinsToOctaveBinsConverter()
) {

    fun find(frequencyBins: FrequencyBins): OctaveBins {
        val peakBins = peakFrequencyBinsFinder.find(frequencyBins)
        val peakBin = peakBins.maxByOrNull { it.magnitude }

        if (peakBin != null) {
            return frequencyBinsToOctaveBinsConverter.convert(listOf(peakBin))
        } else {
            return listOf()
        }

    }

}

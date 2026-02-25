package dsp.bins

import dsp.bins.frequency.FrequencyBins
import dsp.bins.octave.OctaveBins

class FrequencyBinsToOctaveBinsConverter(
    private val frequencyBinToOctaveBinConverter: FrequencyBinToOctaveBinConverter = FrequencyBinToOctaveBinConverter()
) {

    fun convert(frequencyBins: FrequencyBins): OctaveBins {
        return frequencyBins.map {
            frequencyBinToOctaveBinConverter.convert(it)
        }
    }

}

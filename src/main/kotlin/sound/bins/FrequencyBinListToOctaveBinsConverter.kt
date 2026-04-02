package sound.bins

import dsp.bins.FrequencyBins
import sound.bins.octave.OctaveBins

class FrequencyBinsToOctaveBinsConverter(
    private val frequencyBinToOctaveBinConverter: FrequencyBinToOctaveBinConverter = FrequencyBinToOctaveBinConverter()
) {

    fun convert(frequencyBins: FrequencyBins): OctaveBins {
        return frequencyBins.map {
            frequencyBinToOctaveBinConverter.convert(it)
        }
    }

}

package sound.bins

import sound.bins.frequency.FrequencyBins
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

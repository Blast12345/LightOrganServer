package sound.bins

import sound.bins.frequency.FrequencyBinList
import sound.bins.octave.OctaveBinList

class FrequencyBinListToOctaveBinListConverter(
    private val frequencyBinToOctaveBinConverter: FrequencyBinToOctaveBinConverter = FrequencyBinToOctaveBinConverter()
) {

    fun convert(frequencyBins: FrequencyBinList): OctaveBinList {
        return frequencyBins.map {
            frequencyBinToOctaveBinConverter.convert(it)
        }
    }

}

package sound.bins

import dsp.fft.FrequencyBins
import sound.bins.octave.OctaveBins

class FrequencyBinsToOctaveBinsConverter(
    private val frequencyBinToOctaveBinConverter: sound.bins.FrequencyBinToOctaveBinConverter = _root_ide_package_.sound.bins.FrequencyBinToOctaveBinConverter()
) {

    fun convert(frequencyBins: FrequencyBins): sound.bins.octave.OctaveBins {
        return frequencyBins.map {
            frequencyBinToOctaveBinConverter.convert(it)
        }
    }

}

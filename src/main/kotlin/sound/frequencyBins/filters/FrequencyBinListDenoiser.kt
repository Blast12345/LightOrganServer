package sound.frequencyBins.filters

import sound.frequencyBins.FrequencyBinList

class FrequencyBinListDenoiser(
    private val frequencyBinDenoiser: FrequencyBinDenoiser = FrequencyBinDenoiser()
) {

    fun denoise(frequencyBins: FrequencyBinList): FrequencyBinList {
        return frequencyBins.map { frequencyBin ->
            frequencyBinDenoiser.denoise(frequencyBin)
        }
    }

}
package sound.frequencyBins.filters

import sound.frequencyBins.FrequencyBinList

interface FrequencyBinListDenoiserInterface {
    fun denoise(frequencyBins: FrequencyBinList): FrequencyBinList
}

class FrequencyBinListDenoiser(
    private val frequencyBinDenoiser: FrequencyBinDenoiserInterface = FrequencyBinDenoiser()
) : FrequencyBinListDenoiserInterface {

    override fun denoise(frequencyBins: FrequencyBinList): FrequencyBinList {
        return frequencyBins.map { frequencyBin ->
            frequencyBinDenoiser.denoise(frequencyBin)
        }
    }

}
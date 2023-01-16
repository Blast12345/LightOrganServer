package sound.frequencyBins.filters

import sound.frequencyBins.FrequencyBinList

interface BassFrequencyBinsFilterInterface {
    fun filter(frequencyBins: FrequencyBinList): FrequencyBinList
}

class BassFrequencyBinsFilter : BassFrequencyBinsFilterInterface {

    override fun filter(frequencyBins: FrequencyBinList): FrequencyBinList {
        return frequencyBins.filter { it.frequency <= 120.0 }
    }

}
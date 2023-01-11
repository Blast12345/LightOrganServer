package sound.frequencyBins.filters

import sound.frequencyBins.FrequencyBinList

interface BassBinsFilterInterface {
    fun filter(frequencyBins: FrequencyBinList): FrequencyBinList
}

class BassBinsFilter : BassBinsFilterInterface {

    override fun filter(frequencyBins: FrequencyBinList): FrequencyBinList {
        return frequencyBins.filter { it.frequency <= 120.0 }
    }

}
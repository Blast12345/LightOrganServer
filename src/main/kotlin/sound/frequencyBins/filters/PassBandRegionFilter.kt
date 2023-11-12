package sound.frequencyBins.filters

import sound.frequencyBins.FrequencyBinList

class PassBandRegionFilter {

    fun filter(
        frequencyBinList: FrequencyBinList,
        lowStopFrequency: Float,
        highStopFrequency: Float
    ): FrequencyBinList {
        return frequencyBinList.filter {
            it.frequency in lowStopFrequency..highStopFrequency
        }
    }

}
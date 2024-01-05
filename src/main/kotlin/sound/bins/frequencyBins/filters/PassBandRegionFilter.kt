package sound.bins.frequencyBins.filters

import sound.bins.frequencyBins.FrequencyBinList

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
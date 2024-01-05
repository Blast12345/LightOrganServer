package sound.bins.frequency.filters

import sound.bins.frequency.FrequencyBinList

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
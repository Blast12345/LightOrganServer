package sound.bins.frequency.filters

import bins.FrequencyBins

class PassBandRegionFilter {

    fun filter(
        frequencyBins: FrequencyBins,
        lowStopFrequency: Float,
        highStopFrequency: Float
    ): FrequencyBins {
        return frequencyBins.filter {
            it.frequency in lowStopFrequency..highStopFrequency
        }
    }

}

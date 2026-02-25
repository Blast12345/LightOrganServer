package dsp.bins.frequency.filters

import dsp.bins.frequency.FrequencyBins

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

package sound.signalProcessor

import sound.bins.frequency.FrequencyBins

class FrequencyBinsRangeFilter {

    fun filter(
        frequencyBins: FrequencyBins,
        lowestFrequency: Float,
        highestFrequency: Float
    ): FrequencyBins {
        return frequencyBins.filter {
            it.frequency in lowestFrequency..highestFrequency
        }
    }

}

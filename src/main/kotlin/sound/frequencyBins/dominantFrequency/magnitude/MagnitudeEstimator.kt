package sound.frequencyBins.dominantFrequency.magnitude

import sound.frequencyBins.FrequencyBinList

interface MagnitudeEstimator {
    fun estimate(frequency: Float, frequencyBins: FrequencyBinList): Float?
}
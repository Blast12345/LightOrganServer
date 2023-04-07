package lightOrgan.sound.frequencyBins.dominantFrequency.magnitude

import lightOrgan.sound.frequencyBins.FrequencyBinList

interface MagnitudeEstimator {
    fun estimate(frequency: Float, frequencyBins: FrequencyBinList): Float?
}
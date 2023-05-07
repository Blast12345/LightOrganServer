package sound.frequencyBins.dominantFrequency.frequency

import sound.frequencyBins.FrequencyBinList

class WeightedMagnitudeCalculator {

    fun calculate(frequencyBins: FrequencyBinList): Float {
        var weightedMagnitude = 0F

        for (frequencyBin in frequencyBins) {
            weightedMagnitude += frequencyBin.frequency * frequencyBin.magnitude
        }

        return weightedMagnitude
    }

}
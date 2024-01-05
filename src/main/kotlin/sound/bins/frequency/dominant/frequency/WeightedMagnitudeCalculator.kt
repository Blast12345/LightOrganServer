package sound.bins.frequency.dominant.frequency

import sound.bins.frequency.FrequencyBinList

class WeightedMagnitudeCalculator {

    fun calculate(frequencyBins: FrequencyBinList): Float {
        var weightedMagnitude = 0F

        for (frequencyBin in frequencyBins) {
            weightedMagnitude += frequencyBin.frequency * frequencyBin.magnitude
        }

        return weightedMagnitude
    }

}
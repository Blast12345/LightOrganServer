package lightOrgan.sound.frequencyBins.dominantFrequency.frequency

import lightOrgan.sound.frequencyBins.FrequencyBinList

interface WeightedMagnitudeCalculatorInterface {
    fun calculate(frequencyBins: FrequencyBinList): Float
}

class WeightedMagnitudeCalculator : WeightedMagnitudeCalculatorInterface {

    override fun calculate(frequencyBins: FrequencyBinList): Float {
        var weightedMagnitude = 0F

        for (frequencyBin in frequencyBins) {
            weightedMagnitude += frequencyBin.frequency * frequencyBin.magnitude
        }

        return weightedMagnitude
    }

}
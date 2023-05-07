package sound.frequencyBins.dominantFrequency.frequency

import sound.frequencyBins.FrequencyBinList

class TotalMagnitudeCalculator {

    fun calculate(frequencyBins: FrequencyBinList): Float {
        return frequencyBins.map { it.magnitude }.sum()
    }

}
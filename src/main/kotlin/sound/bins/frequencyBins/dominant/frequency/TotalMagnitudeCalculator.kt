package sound.bins.frequencyBins.dominant.frequency

import sound.bins.frequencyBins.FrequencyBinList

class TotalMagnitudeCalculator {

    fun calculate(frequencyBins: FrequencyBinList): Float {
        return frequencyBins.map { it.magnitude }.sum()
    }

}
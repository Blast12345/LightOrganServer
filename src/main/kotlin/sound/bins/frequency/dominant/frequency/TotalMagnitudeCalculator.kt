package sound.bins.frequency.dominant.frequency

import sound.bins.frequency.FrequencyBinList

class TotalMagnitudeCalculator {

    fun calculate(frequencyBins: FrequencyBinList): Float {
        return frequencyBins.map { it.magnitude }.sum()
    }

}
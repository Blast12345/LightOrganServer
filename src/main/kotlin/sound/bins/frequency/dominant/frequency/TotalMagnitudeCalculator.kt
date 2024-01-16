package sound.bins.frequency.dominant.frequency

import sound.bins.frequency.FrequencyBins

class TotalMagnitudeCalculator {

    fun calculate(frequencyBins: FrequencyBins): Float {
        return frequencyBins.map { it.magnitude }.sum()
    }

}

package sound.bins.frequency.dominant.magnitude

import sound.bins.frequency.FrequencyBinList

class DominantMagnitudeCalculator {

    fun calculate(frequencyBins: FrequencyBinList): Float? {
        return frequencyBins.maxOfOrNull { it.magnitude }
    }

}

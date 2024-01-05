package sound.bins.frequencyBins.dominant.magnitude

import sound.bins.frequencyBins.FrequencyBinList

class DominantMagnitudeCalculator {

    fun calculate(frequencyBins: FrequencyBinList): Float? {
        return frequencyBins.maxOfOrNull { it.magnitude }
    }

}
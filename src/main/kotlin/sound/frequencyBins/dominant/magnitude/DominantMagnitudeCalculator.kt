package sound.frequencyBins.dominant.magnitude

import sound.frequencyBins.FrequencyBinList

class DominantMagnitudeCalculator {

    fun calculate(frequencyBins: FrequencyBinList): Float? {
        return frequencyBins.maxOfOrNull { it.magnitude }
    }

}
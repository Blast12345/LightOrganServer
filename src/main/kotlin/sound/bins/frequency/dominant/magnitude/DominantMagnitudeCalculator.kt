package sound.bins.frequency.dominant.magnitude

import sound.bins.frequency.FrequencyBins

class DominantMagnitudeCalculator {

    fun calculate(frequencyBins: FrequencyBins): Float? {
        return frequencyBins.maxOfOrNull { it.magnitude }
    }

}

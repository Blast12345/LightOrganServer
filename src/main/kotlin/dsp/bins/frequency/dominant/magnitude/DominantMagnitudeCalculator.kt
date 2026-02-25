package dsp.bins.frequency.dominant.magnitude

import dsp.bins.frequency.FrequencyBins

class DominantMagnitudeCalculator {

    fun calculate(frequencyBins: FrequencyBins): Float? {
        return frequencyBins.maxOfOrNull { it.magnitude }
    }

}

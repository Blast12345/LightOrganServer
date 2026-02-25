package dsp.bins.frequency.dominant.frequency

import dsp.bins.frequency.FrequencyBins

class TotalMagnitudeCalculator {

    fun calculate(frequencyBins: FrequencyBins): Float {
        return frequencyBins.map { it.magnitude }.sum()
    }

}

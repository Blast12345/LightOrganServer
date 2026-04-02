package sound.bins.frequency.dominant.frequency

import dsp.bins.FrequencyBins

class TotalMagnitudeCalculator {

    fun calculate(frequencyBins: FrequencyBins): Float {
        return frequencyBins.map { it.magnitude }.sum()
    }

}

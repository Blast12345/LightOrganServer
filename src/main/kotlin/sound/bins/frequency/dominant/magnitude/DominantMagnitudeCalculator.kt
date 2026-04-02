package sound.bins.frequency.dominant.magnitude

import dsp.bins.FrequencyBins

class DominantMagnitudeCalculator {

    fun calculate(frequencyBins: FrequencyBins): Float? {
        return frequencyBins.maxOfOrNull { it.magnitude }
    }

}

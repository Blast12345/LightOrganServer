package sound.bins.frequency.dominant.frequency

import dsp.fft.FrequencyBins

class TotalMagnitudeCalculator {

    fun calculate(frequencyBins: FrequencyBins): Float {
        return frequencyBins.map { it.magnitude }.sum()
    }

}

package sound.bins.frequency.dominant.frequency

import dsp.fft.FrequencyBins

class WeightedMagnitudeCalculator {

    fun calculate(frequencyBins: FrequencyBins): Float {
        var weightedMagnitude = 0F

        for (frequencyBin in frequencyBins) {
            weightedMagnitude += frequencyBin.frequency * frequencyBin.magnitude
        }

        return weightedMagnitude
    }

}

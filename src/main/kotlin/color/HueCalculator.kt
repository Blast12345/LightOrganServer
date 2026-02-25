package color

import dsp.bins.frequency.FrequencyBins
import dsp.bins.octave.OctaveWeightedAverageCalculator
import dsp.bins.octave.PeakOctaveBinsFinder

class HueCalculator(
    private val peakOctaveBinsFinder: PeakOctaveBinsFinder = PeakOctaveBinsFinder(),
    private val octaveWeightedAverageCalculator: OctaveWeightedAverageCalculator = OctaveWeightedAverageCalculator()
) {

    fun calculate(frequencyBins: FrequencyBins): Float? {
        val peakOctaveBins = peakOctaveBinsFinder.find(frequencyBins)
        return octaveWeightedAverageCalculator.calculate(peakOctaveBins)
    }

}

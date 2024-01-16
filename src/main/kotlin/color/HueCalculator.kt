package color

import sound.bins.frequency.FrequencyBins
import sound.bins.octave.OctaveWeightedAverageCalculator
import sound.bins.octave.PeakOctaveBinsFinder

class HueCalculator(
    private val peakOctaveBinsFinder: PeakOctaveBinsFinder = PeakOctaveBinsFinder(),
    private val octaveWeightedAverageCalculator: OctaveWeightedAverageCalculator = OctaveWeightedAverageCalculator()
) {

    fun calculate(frequencyBins: FrequencyBins): Float? {
        val peakOctaveBins = peakOctaveBinsFinder.find(frequencyBins)
        return octaveWeightedAverageCalculator.calculate(peakOctaveBins)
    }

}

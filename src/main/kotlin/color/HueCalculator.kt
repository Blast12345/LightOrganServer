package color

import sound.frequencyBins.FrequencyBinList
import sound.octave.OctaveWeightedAverageCalculator
import sound.octave.PeakOctaveBinsFinder

class HueCalculator(
    private val peakOctaveBinsFinder: PeakOctaveBinsFinder = PeakOctaveBinsFinder(),
    private val octaveWeightedAverageCalculator: OctaveWeightedAverageCalculator = OctaveWeightedAverageCalculator()
) {

    fun calculate(frequencyBins: FrequencyBinList): Float? {
        val peakOctaveBins = peakOctaveBinsFinder.find(frequencyBins)
        return octaveWeightedAverageCalculator.calculate(peakOctaveBins)
    }

}
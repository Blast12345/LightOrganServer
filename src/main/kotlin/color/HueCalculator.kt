package color

import dsp.fft.FrequencyBins
import sound.bins.octave.OctaveWeightedAverageCalculator
import sound.bins.octave.PeakOctaveBinsFinder

class HueCalculator(
    private val peakOctaveBinsFinder: sound.bins.octave.PeakOctaveBinsFinder = _root_ide_package_.sound.bins.octave.PeakOctaveBinsFinder(),
    private val octaveWeightedAverageCalculator: sound.bins.octave.OctaveWeightedAverageCalculator = _root_ide_package_.sound.bins.octave.OctaveWeightedAverageCalculator()
) {

    fun calculate(frequencyBins: FrequencyBins): Float? {
        val peakOctaveBins = peakOctaveBinsFinder.find(frequencyBins)
        return octaveWeightedAverageCalculator.calculate(peakOctaveBins)
    }

}

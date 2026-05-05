package lightOrgan.color

import dsp.bins.FrequencyBins

// TODO: Move me
// ENHANCEMENT: Noise floor rejection
class PeakFrequencyBinsFinder(
    private val peakFrequencyBinsCalculator: PeakFrequencyBinsCalculator = PeakFrequencyBinsCalculator(),
    private val prominenceCalculator: ProminenceCalculator = ProminenceCalculator()
) {

    fun find(frequencyBins: FrequencyBins): FrequencyBins {
        val peakBins = peakFrequencyBinsCalculator.calculate(frequencyBins)
        val peaksWithProminence = prominenceCalculator.calculate(peakBins, frequencyBins)
        return peaksWithProminence.filter { it.prominence > 0.0005 }.map { it.frequencyBin }
    }

}
package color

import config.ConfigSingleton
import dsp.fft.FrequencyBins
import sound.bins.FrequencyBinsToOctaveBinsConverter
import sound.bins.frequency.dominant.frequency.PeakFrequencyBinsCalculator
import sound.bins.frequency.dominant.frequency.PeakFrequencyBinsFinder
import sound.bins.frequency.filters.Crossover
import sound.bins.octave.OctaveWeightedAverageCalculator

class HueCalculator(
    private val lowCrossover: Crossover = ConfigSingleton.lowCrossover,
    private val highCrossover: Crossover = ConfigSingleton.highCrossover,
    private val peakFrequencyBinsFinder: PeakFrequencyBinsFinder = PeakFrequencyBinsFinder(),
    private val peakFrequencyBinsCalculator: PeakFrequencyBinsCalculator = PeakFrequencyBinsCalculator(),
    private val frequencyBinsToOctaveBinsConverter: FrequencyBinsToOctaveBinsConverter = FrequencyBinsToOctaveBinsConverter(),
    private val octaveWeightedAverageCalculator: OctaveWeightedAverageCalculator = OctaveWeightedAverageCalculator()
) {

    // TODO: Filter
    fun calculate(frequencyBins: FrequencyBins): Float? {
        val bassBins = frequencyBins
            .filter { lowCrossover.stopFrequency <= it.frequency }
            .filter { it.frequency <= highCrossover.stopFrequency }
//        println(bassBins[21])
//        println(bassBins[22])
//        println(bassBins[23])
//        println(bassBins[24])

//        val peakBassBins = peakFrequencyBinsFinder.find(bassBins) // TODO: Use parabolic function
        val peakBassBins = peakFrequencyBinsCalculator.calculate(bassBins)
        println(peakBassBins.firstOrNull()?.frequency)

        val peakOctaveBins = frequencyBinsToOctaveBinsConverter.convert(peakBassBins)

        return octaveWeightedAverageCalculator.calculate(peakOctaveBins)
    }

}

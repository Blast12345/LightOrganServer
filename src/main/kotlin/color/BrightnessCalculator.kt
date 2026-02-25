package color

import config.ConfigSingleton
import dsp.bins.frequency.FrequencyBins
import dsp.bins.frequency.GreatestMagnitudeFinder
import dsp.bins.frequency.filters.BandPassFilter
import dsp.bins.frequency.filters.Crossover

class BrightnessCalculator(
    private val bandPassFilter: BandPassFilter = BandPassFilter(),
    private val lowCrossover: Crossover = ConfigSingleton.lowCrossover,
    private val highCrossover: Crossover = ConfigSingleton.highCrossover,
    private val greatestMagnitudeFinder: GreatestMagnitudeFinder = GreatestMagnitudeFinder(),
) {

    fun calculate(frequencyBins: FrequencyBins): Float? {
        val magnitude = calculateMagnitude(frequencyBins) ?: return null
        val adjustedMagnitude = magnitude // TODO: How to scale?

        return if (adjustedMagnitude < 1F) {
            adjustedMagnitude
        } else {
            1F
        }
    }

    private fun calculateMagnitude(frequencyBins: FrequencyBins): Float? {
        val filteredBins = getFilteredBins(frequencyBins)
        return greatestMagnitudeFinder.find(filteredBins)
    }

    private fun getFilteredBins(frequencyBins: FrequencyBins): FrequencyBins {
        return bandPassFilter.filter(
            frequencyBins = frequencyBins,
            lowCrossover = lowCrossover,
            highCrossover = highCrossover
        )
    }

}


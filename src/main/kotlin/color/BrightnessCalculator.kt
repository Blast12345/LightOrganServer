package color

import config.ConfigSingleton
import dsp.fft.FrequencyBins
import sound.bins.frequency.GreatestMagnitudeFinder
import sound.bins.frequency.filters.BandPassFilter
import sound.bins.frequency.filters.Crossover

class BrightnessCalculator(
    private val bandPassFilter: sound.bins.frequency.filters.BandPassFilter = _root_ide_package_.sound.bins.frequency.filters.BandPassFilter(),
    private val lowCrossover: sound.bins.frequency.filters.Crossover = ConfigSingleton.lowCrossover,
    private val highCrossover: sound.bins.frequency.filters.Crossover = ConfigSingleton.highCrossover,
    private val greatestMagnitudeFinder: sound.bins.frequency.GreatestMagnitudeFinder = _root_ide_package_.sound.bins.frequency.GreatestMagnitudeFinder(),
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


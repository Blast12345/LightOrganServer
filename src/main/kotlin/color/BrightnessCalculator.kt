package color

import config.ConfigSingleton
import sound.bins.frequency.FrequencyBinList
import sound.bins.frequency.GreatestMagnitudeFinder
import sound.bins.frequency.filters.BandPassFilter
import sound.bins.frequency.filters.Crossover

class BrightnessCalculator(
    private val bandPassFilter: BandPassFilter = BandPassFilter(),
    private val lowCrossover: Crossover = ConfigSingleton.lowCrossover,
    private val highCrossover: Crossover = ConfigSingleton.highCrossover,
    private val greatestMagnitudeFinder: GreatestMagnitudeFinder = GreatestMagnitudeFinder(),
) {

    fun calculate(frequencyBins: FrequencyBinList): Float? {
        val magnitude = calculateMagnitude(frequencyBins) ?: return null

        return if (magnitude < 1F) {
            magnitude
        } else {
            1F
        }
    }

    private fun calculateMagnitude(frequencyBins: FrequencyBinList): Float? {
        val filteredBins = getFilteredBins(frequencyBins)
        return greatestMagnitudeFinder.find(filteredBins)
    }

    private fun getFilteredBins(frequencyBins: FrequencyBinList): FrequencyBinList {
        return bandPassFilter.filter(
            frequencyBinList = frequencyBins,
            lowCrossover = lowCrossover,
            highCrossover = highCrossover
        )
    }

}


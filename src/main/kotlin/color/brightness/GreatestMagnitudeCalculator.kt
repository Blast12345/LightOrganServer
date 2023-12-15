package color.brightness

import config.ConfigSingleton
import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.filters.BandPassFilter
import sound.frequencyBins.filters.Crossover

class GreatestMagnitudeCalculator(
    private val bandPassFilter: BandPassFilter = BandPassFilter(),
    private val lowCrossover: Crossover = ConfigSingleton.lowCrossover,
    private val highCrossover: Crossover = ConfigSingleton.highCrossover,
) {

    fun calculate(frequencyBins: FrequencyBinList): Float? {
        val filteredBins = getFilteredBins(frequencyBins)
        return filteredBins.maxOfOrNull { it.magnitude }
    }

    private fun getFilteredBins(frequencyBins: FrequencyBinList): FrequencyBinList {
        return bandPassFilter.filter(
            frequencyBinList = frequencyBins,
            lowCrossover = lowCrossover,
            highCrossover = highCrossover
        )
    }

}
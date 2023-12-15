package color

import config.ConfigSingleton
import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.GreatestMagnitudeCalculator
import sound.frequencyBins.filters.BandPassFilter
import sound.frequencyBins.filters.Crossover

class BrightnessCalculator(
    private val bandPassFilter: BandPassFilter = BandPassFilter(),
    private val lowCrossover: Crossover = ConfigSingleton.lowCrossover,
    private val highCrossover: Crossover = ConfigSingleton.highCrossover,
    private val greatestMagnitudeCalculator: GreatestMagnitudeCalculator = GreatestMagnitudeCalculator(),
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
        return greatestMagnitudeCalculator.calculate(filteredBins)
    }

    private fun getFilteredBins(frequencyBins: FrequencyBinList): FrequencyBinList {
        return bandPassFilter.filter(
            frequencyBinList = frequencyBins,
            lowCrossover = lowCrossover,
            highCrossover = highCrossover
        )
    }

}


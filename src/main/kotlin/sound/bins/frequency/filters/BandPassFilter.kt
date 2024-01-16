package sound.bins.frequency.filters

import sound.bins.frequency.FrequencyBins

class BandPassFilter(
    private val crossoverFilter: CrossoverFilter = CrossoverFilter()
) {

    fun filter(
        frequencyBins: FrequencyBins,
        lowCrossover: Crossover,
        highCrossover: Crossover
    ): FrequencyBins {
        return frequencyBins
            .filter(lowCrossover)
            .filter(highCrossover)
    }

    private fun FrequencyBins.filter(filter: Crossover): FrequencyBins {
        return crossoverFilter.filter(this, filter)
    }

}

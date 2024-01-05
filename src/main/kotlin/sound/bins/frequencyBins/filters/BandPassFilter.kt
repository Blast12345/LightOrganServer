package sound.bins.frequencyBins.filters

import sound.bins.frequencyBins.FrequencyBinList

class BandPassFilter(
    private val crossoverFilter: CrossoverFilter = CrossoverFilter()
) {

    fun filter(
        frequencyBinList: FrequencyBinList,
        lowCrossover: Crossover,
        highCrossover: Crossover
    ): FrequencyBinList {
        return frequencyBinList
            .filter(lowCrossover)
            .filter(highCrossover)
    }

    private fun FrequencyBinList.filter(filter: Crossover): FrequencyBinList {
        return crossoverFilter.filter(this, filter)
    }

}
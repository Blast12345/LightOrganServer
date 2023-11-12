package sound.frequencyBins.filters

import sound.frequencyBins.FrequencyBinList

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
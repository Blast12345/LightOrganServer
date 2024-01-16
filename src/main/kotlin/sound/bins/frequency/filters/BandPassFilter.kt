package sound.bins.frequency.filters

import sound.bins.frequency.FrequencyBinList

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

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
            .applyFilter(lowCrossover)
            .applyFilter(highCrossover)
    }

    private fun FrequencyBinList.applyFilter(filter: Crossover): FrequencyBinList {
        return crossoverFilter.filter(this, filter)
    }

}
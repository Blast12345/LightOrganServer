package sound.frequencyBins.filters

import extensions.between
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import kotlin.math.min

// TODO: Find a more natural filter
class BandPassFilter {

    fun filter(
        frequencyBinList: FrequencyBinList,
        lowCrossover: Crossover,
        highCrossover: Crossover
    ): FrequencyBinList {
        return frequencyBinList
            .applyFilter(lowCrossover)
            .applyFilter(highCrossover)
        // TODO: Trim bins
    }

    private fun FrequencyBinList.applyFilter(filter: Crossover): FrequencyBinList {
        val filteredBins = mutableListOf<FrequencyBin>()

        for (bin in this) {
            val range = filter.cornerFrequency - filter.stopFrequency
            val rawMultiplier = (bin.frequency - filter.stopFrequency) / range
            val multiplier = rawMultiplier.between(0F, 1F)
            val filteredMagnitude = bin.magnitude * min(multiplier, 1F)
            filteredBins.add(FrequencyBin(bin.frequency, filteredMagnitude))
        }

        return filteredBins
    }

}
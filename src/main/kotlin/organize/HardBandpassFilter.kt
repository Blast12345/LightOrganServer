package organize

import sound.bins.frequency.FrequencyBins
import sound.bins.frequency.filters.Crossover

class HardBandpassFilter {

    fun filter(
        frequencyBins: FrequencyBins,
        lowCrossover: Crossover,
        highCrossover: Crossover
    ): FrequencyBins {
        // TODO: Filter
        val filteredBins = frequencyBins

        return filteredBins.filter {
            it.frequency in lowCrossover.stopFrequency..highCrossover.stopFrequency
        }
    }

}

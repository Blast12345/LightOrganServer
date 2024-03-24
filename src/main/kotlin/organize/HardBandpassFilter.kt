package organize

import sound.bins.frequency.FrequencyBins
import sound.bins.frequency.filters.BandPassFilter
import sound.bins.frequency.filters.Crossover

// TODO:
class HardBandpassFilter(
    private val bandPassFilter: BandPassFilter = BandPassFilter()
) {

    fun filter(
        frequencyBins: FrequencyBins,
        lowCrossover: Crossover,
        highCrossover: Crossover
    ): FrequencyBins {
        val filteredBins = bandPassFilter.filter(frequencyBins, lowCrossover, highCrossover)

        return filteredBins.filter {
            it.frequency in lowCrossover.stopFrequency..highCrossover.stopFrequency
        }
    }

}

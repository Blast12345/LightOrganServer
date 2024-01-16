package sound.bins.frequency.dominant

import config.ConfigSingleton
import sound.bins.frequency.FrequencyBin
import sound.bins.frequency.FrequencyBins
import sound.bins.frequency.dominant.frequency.DominantFrequencyCalculator
import sound.bins.frequency.dominant.magnitude.DominantMagnitudeCalculator
import sound.bins.frequency.filters.BandPassFilter
import sound.bins.frequency.filters.Crossover
import sound.bins.frequency.filters.PassBandRegionFilter

class DominantBassFrequencyBinCalculator(
    private val passBandRegionFilter: PassBandRegionFilter = PassBandRegionFilter(),
    private val bandPassFilter: BandPassFilter = BandPassFilter(),
    private val dominantFrequencyCalculator: DominantFrequencyCalculator = DominantFrequencyCalculator(),
    private val dominantMagnitudeCalculator: DominantMagnitudeCalculator = DominantMagnitudeCalculator(),
    private val lowCrossover: Crossover = ConfigSingleton.lowCrossover,
    private val highCrossover: Crossover = ConfigSingleton.highCrossover
) {

    @Suppress("ReturnCount")
    fun calculate(frequencyBins: FrequencyBins): FrequencyBin? {
        val bassBins = getBassBins(frequencyBins)

        val frequency = getDominantFrequency(bassBins) ?: return null
        val magnitude = getDominantMagnitude(bassBins) ?: return null

        return FrequencyBin(frequency, magnitude)
    }

    private fun getBassBins(frequencyBins: FrequencyBins): FrequencyBins {
        return passBandRegionFilter.filter(
            frequencyBins = frequencyBins,
            lowStopFrequency = lowCrossover.stopFrequency,
            highStopFrequency = highCrossover.stopFrequency
        )
    }

    private fun getDominantFrequency(frequencyBins: FrequencyBins): Float? {
        return dominantFrequencyCalculator.calculate(frequencyBins)
    }

    private fun getDominantMagnitude(frequencyBins: FrequencyBins): Float? {
        val filteredBins = getFilteredBins(frequencyBins)
        return dominantMagnitudeCalculator.calculate(filteredBins)
    }

    private fun getFilteredBins(frequencyBins: FrequencyBins): FrequencyBins {
        return bandPassFilter.filter(
            frequencyBins = frequencyBins,
            lowCrossover = lowCrossover,
            highCrossover = highCrossover
        )
    }

}

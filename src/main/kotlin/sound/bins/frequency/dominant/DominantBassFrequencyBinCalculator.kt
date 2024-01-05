package sound.bins.frequency.dominant

import config.ConfigSingleton
import sound.bins.frequency.FrequencyBin
import sound.bins.frequency.FrequencyBinList
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

    fun calculate(frequencyBinList: FrequencyBinList): FrequencyBin? {
        val bassBins = getBassBins(frequencyBinList)

        val frequency = getDominantFrequency(bassBins) ?: return null
        val magnitude = getDominantMagnitude(bassBins) ?: return null

        return FrequencyBin(frequency, magnitude)
    }

    private fun getBassBins(frequencyBins: FrequencyBinList): FrequencyBinList {
        return passBandRegionFilter.filter(
            frequencyBinList = frequencyBins,
            lowStopFrequency = lowCrossover.stopFrequency,
            highStopFrequency = highCrossover.stopFrequency
        )
    }

    private fun getDominantFrequency(frequencyBins: FrequencyBinList): Float? {
        return dominantFrequencyCalculator.calculate(frequencyBins)
    }

    private fun getDominantMagnitude(frequencyBins: FrequencyBinList): Float? {
        val filteredBins = getFilteredBins(frequencyBins)
        return dominantMagnitudeCalculator.calculate(filteredBins)
    }

    private fun getFilteredBins(frequencyBins: FrequencyBinList): FrequencyBinList {
        return bandPassFilter.filter(
            frequencyBinList = frequencyBins,
            lowCrossover = lowCrossover,
            highCrossover = highCrossover
        )
    }

}


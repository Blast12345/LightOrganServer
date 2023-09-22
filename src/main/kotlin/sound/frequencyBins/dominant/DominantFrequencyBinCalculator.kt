package sound.frequencyBins.dominant

import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.dominant.frequency.DominantFrequencyCalculator
import sound.frequencyBins.dominant.magnitude.DominantMagnitudeCalculator

class DominantFrequencyBinCalculator(
    private val dominantFrequencyCalculator: DominantFrequencyCalculator = DominantFrequencyCalculator(),
    private val dominantMagnitudeCalculator: DominantMagnitudeCalculator = DominantMagnitudeCalculator()
) {

    fun calculate(frequencyBinList: FrequencyBinList): FrequencyBin? {
        val frequency = getDominantFrequency(frequencyBinList) ?: return null
        val magnitude = getDominantMagnitude(frequencyBinList) ?: return null
        return FrequencyBin(frequency, magnitude)
    }

    private fun getDominantFrequency(frequencyBins: FrequencyBinList): Float? {
        return dominantFrequencyCalculator.calculate(frequencyBins)
    }

    private fun getDominantMagnitude(frequencyBins: FrequencyBinList): Float? {
        return dominantMagnitudeCalculator.calculate(frequencyBins)
    }

}


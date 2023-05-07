package sound.frequencyBins.dominantFrequency

import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.dominantFrequency.frequency.DominantFrequencyCalculator
import sound.frequencyBins.dominantFrequency.magnitude.AveragePeakMagnitudeEstimator
import sound.frequencyBins.dominantFrequency.magnitude.MagnitudeEstimator

class DominantFrequencyBinFactory(
    private val dominantFrequencyCalculator: DominantFrequencyCalculator = DominantFrequencyCalculator(),
    private val magnitudeEstimator: MagnitudeEstimator = AveragePeakMagnitudeEstimator()
) {

    fun create(frequencyBins: FrequencyBinList): FrequencyBin? {
        val dominantFrequency = getDominantFrequency(frequencyBins) ?: return null
        val estimatedMagnitude = getEstimatedMagnitude(dominantFrequency, frequencyBins) ?: return null
        return FrequencyBin(dominantFrequency, estimatedMagnitude)
    }

    private fun getDominantFrequency(frequencyBins: FrequencyBinList): Float? {
        return dominantFrequencyCalculator.calculate(frequencyBins)
    }

    private fun getEstimatedMagnitude(frequency: Float, frequencyBins: FrequencyBinList): Float? {
        return magnitudeEstimator.estimate(frequency, frequencyBins)
    }

}
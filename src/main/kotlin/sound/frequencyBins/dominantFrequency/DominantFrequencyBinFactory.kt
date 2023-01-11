package sound.frequencyBins.dominantFrequency

import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList

interface DominantFrequencyBinFactoryInterface {
    fun create(frequencyBins: FrequencyBinList): FrequencyBin?
}

class DominantFrequencyBinFactory(
    private val dominantFrequencyCalculator: DominantFrequencyCalculatorInterface = DominantFrequencyCalculator(),
    private val magnitudeInterpolator: MagnitudeEstimatorInterface = MagnitudeEstimator(),
) : DominantFrequencyBinFactoryInterface {

    override fun create(frequencyBins: FrequencyBinList): FrequencyBin? {
        val dominantFrequency = getDominantFrequency(frequencyBins) ?: return null
        val estimatedMagnitude = getEstimatedMagnitude(dominantFrequency, frequencyBins) ?: return null
        return FrequencyBin(dominantFrequency, estimatedMagnitude)
    }

    private fun getDominantFrequency(frequencyBins: FrequencyBinList): Float? {
        return dominantFrequencyCalculator.calculate(frequencyBins)
    }

    private fun getEstimatedMagnitude(frequency: Float, frequencyBins: FrequencyBinList): Float? {
        return magnitudeInterpolator.estimate(frequency, frequencyBins)
    }

}
package lightOrgan.sound.frequencyBins.dominantFrequency

import lightOrgan.sound.frequencyBins.FrequencyBin
import lightOrgan.sound.frequencyBins.FrequencyBinList
import lightOrgan.sound.frequencyBins.dominantFrequency.frequency.DominantFrequencyCalculator
import lightOrgan.sound.frequencyBins.dominantFrequency.frequency.DominantFrequencyCalculatorInterface
import lightOrgan.sound.frequencyBins.dominantFrequency.magnitude.MagnitudeEstimator
import lightOrgan.sound.frequencyBins.dominantFrequency.magnitude.averagePeakStrategy.MagnitudeAveragePeakEstimator

interface DominantFrequencyBinFactoryInterface {
    fun create(frequencyBins: FrequencyBinList): FrequencyBin?
}

class DominantFrequencyBinFactory(
    private val dominantFrequencyCalculator: DominantFrequencyCalculatorInterface = DominantFrequencyCalculator(),
    private val magnitudeEstimator: MagnitudeEstimator = MagnitudeAveragePeakEstimator()
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
        return magnitudeEstimator.estimate(frequency, frequencyBins)
    }

}
package sound.frequencyBins.dominantFrequency

import config.Config
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.dominantFrequency.frequency.DominantFrequencyCalculator
import sound.frequencyBins.dominantFrequency.frequency.DominantFrequencyCalculatorInterface
import sound.frequencyBins.dominantFrequency.magnitude.MagnitudeEstimator
import sound.frequencyBins.dominantFrequency.magnitude.averagePeakStrategy.MagnitudeAveragePeakEstimator

interface DominantFrequencyBinFactoryInterface {
    fun create(frequencyBins: FrequencyBinList): FrequencyBin?
}

class DominantFrequencyBinFactory(
    config: Config,
    private val dominantFrequencyCalculator: DominantFrequencyCalculatorInterface = DominantFrequencyCalculator(),
    private val magnitudeEstimator: MagnitudeEstimator = MagnitudeAveragePeakEstimator(config)
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
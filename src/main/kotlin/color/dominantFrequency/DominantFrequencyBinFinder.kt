package color.dominantFrequency

import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList

interface DominantFrequencyBinFinderInterface {
    fun find(frequencyBins: FrequencyBinList): FrequencyBin
}

// TODO: Test me
// TODO: Rename to Factory? Or maybe Estimator?
class DominantFrequencyBinFinder(
    // TODO: dominant frequency calculator?
    private val averageFrequencyCalculator: AverageFrequencyCalculatorInterface = AverageFrequencyCalculator(),
    private val magnitudeEstimator: MagnitudeEstimatorInterface = MagnitudeEstimator(),
) : DominantFrequencyBinFinderInterface {

    override fun find(frequencyBins: FrequencyBinList): FrequencyBin {
        // TODO: Given the poor frequency resolution, average is not the best algorithm.
        // Try peak frequency or increasing the magnitudes by some power.
        val averageFrequency = getAverageFrequency(frequencyBins)
        val estimatedMagnitude = getEstimatedMagnitude(averageFrequency, frequencyBins)
        return FrequencyBin(averageFrequency, 0.5F) // TODO: Magnitude logic
    }

    private fun getAverageFrequency(frequencyBins: FrequencyBinList): Float {
        // TODO: Test for default
        return averageFrequencyCalculator.calculate(frequencyBins) ?: 0F
    }

    private fun getEstimatedMagnitude(frequency: Float, frequencyBins: FrequencyBinList): Float {
        return magnitudeEstimator.estimate(frequency, frequencyBins)
    }

}
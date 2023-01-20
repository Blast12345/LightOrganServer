package sound.frequencyBins.dominantFrequency.frequency

import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.finders.FrequencyBinFinder
import sound.frequencyBins.finders.FrequencyBinFinderInterface

interface DominantFrequencyCalculatorInterface {
    fun calculate(frequencyBins: FrequencyBinList): Float?
}

class DominantFrequencyCalculator(
    private val frequencyBinFinder: FrequencyBinFinderInterface = FrequencyBinFinder(),
    private val weightedMagnitudeCalculator: WeightedMagnitudeCalculatorInterface = WeightedMagnitudeCalculator(),
    private val totalMagnitudeCalculator: TotalMagnitudeCalculatorInterface = TotalMagnitudeCalculator()
) : DominantFrequencyCalculatorInterface {

    override fun calculate(frequencyBins: FrequencyBinList): Float? {
        val peakBins = getPeakFrequencyBins(frequencyBins)
        val weightedMagnitude = weightedMagnitude(peakBins)
        val totalMagnitude = totalMagnitude(peakBins)
        return averageFrequency(weightedMagnitude, totalMagnitude)
    }

    private fun getPeakFrequencyBins(frequencyBins: FrequencyBinList): FrequencyBinList {
        return frequencyBinFinder.findPeaks(frequencyBins)
    }

    private fun weightedMagnitude(frequencyBins: FrequencyBinList): Float {
        return weightedMagnitudeCalculator.calculate(frequencyBins)
    }

    private fun totalMagnitude(frequencyBins: FrequencyBinList): Float {
        return totalMagnitudeCalculator.calculate(frequencyBins)
    }

    private fun averageFrequency(weightedMagnitude: Float, totalMagnitude: Float): Float? {
        return if (totalMagnitude == 0F) {
            null
        } else {
            weightedMagnitude / totalMagnitude
        }
    }

}
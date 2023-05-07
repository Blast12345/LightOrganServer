package sound.frequencyBins.dominantFrequency.frequency

import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.finders.FrequencyBinFinder

class DominantFrequencyCalculator(
    private val frequencyBinFinder: FrequencyBinFinder = FrequencyBinFinder(),
    private val weightedMagnitudeCalculator: WeightedMagnitudeCalculator = WeightedMagnitudeCalculator(),
    private val totalMagnitudeCalculator: TotalMagnitudeCalculator = TotalMagnitudeCalculator()
) {

    fun calculate(frequencyBins: FrequencyBinList): Float? {
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
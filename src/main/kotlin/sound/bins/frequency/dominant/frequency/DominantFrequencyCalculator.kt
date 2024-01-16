package sound.bins.frequency.dominant.frequency

import sound.bins.frequency.FrequencyBins

class DominantFrequencyCalculator(
    private val peakFrequencyBinsFinder: PeakFrequencyBinsFinder = PeakFrequencyBinsFinder(),
    private val weightedMagnitudeCalculator: WeightedMagnitudeCalculator = WeightedMagnitudeCalculator(),
    private val totalMagnitudeCalculator: TotalMagnitudeCalculator = TotalMagnitudeCalculator()
) {

    fun calculate(frequencyBins: FrequencyBins): Float? {
        val peakBins = getPeakFrequencyBins(frequencyBins)
        val weightedMagnitude = weightedMagnitude(peakBins)
        val totalMagnitude = totalMagnitude(peakBins)
        return averageFrequency(weightedMagnitude, totalMagnitude)
    }

    private fun getPeakFrequencyBins(frequencyBins: FrequencyBins): FrequencyBins {
        return peakFrequencyBinsFinder.find(frequencyBins)
    }

    private fun weightedMagnitude(frequencyBins: FrequencyBins): Float {
        return weightedMagnitudeCalculator.calculate(frequencyBins)
    }

    private fun totalMagnitude(frequencyBins: FrequencyBins): Float {
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

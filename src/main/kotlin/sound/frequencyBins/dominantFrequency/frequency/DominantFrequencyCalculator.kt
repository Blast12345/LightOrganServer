package sound.frequencyBins.dominantFrequency.frequency

import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.finders.FrequencyBinFinder
import sound.frequencyBins.finders.FrequencyBinFinderInterface

interface DominantFrequencyCalculatorInterface {
    fun calculate(frequencyBins: FrequencyBinList): Float?
}

class DominantFrequencyCalculator(
    private val frequencyBinFinder: FrequencyBinFinderInterface = FrequencyBinFinder()
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

    // TODO: Extract
    private fun weightedMagnitude(frequencyBins: FrequencyBinList): Float {
        var weightedMagnitude = 0F

        for (frequencyBin in frequencyBins) {
            weightedMagnitude += frequencyBin.frequency * frequencyBin.magnitude
        }

        return weightedMagnitude
    }

    // TODO: Extract
    private fun totalMagnitude(frequencyBins: FrequencyBinList): Float {
        return frequencyBins.map { it.magnitude }.sum()
    }

    private fun averageFrequency(weightedMagnitude: Float, totalMagnitude: Float): Float? {
        return if (totalMagnitude == 0F) {
            null
        } else {
            weightedMagnitude / totalMagnitude
        }
    }

}
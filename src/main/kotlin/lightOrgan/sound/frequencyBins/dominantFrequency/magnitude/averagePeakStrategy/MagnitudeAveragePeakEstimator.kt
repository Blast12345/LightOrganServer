package lightOrgan.sound.frequencyBins.dominantFrequency.magnitude.averagePeakStrategy

import config.PersistedConfig
import lightOrgan.sound.frequencyBins.FrequencyBinList
import lightOrgan.sound.frequencyBins.dominantFrequency.magnitude.MagnitudeEstimator

class MagnitudeAveragePeakEstimator(
    private val numberOfPeaksToUse: Int = PersistedConfig().magnitudeEstimationStrategy.numberOfPeaksToUse
) : MagnitudeEstimator {

    override fun estimate(frequency: Float, frequencyBins: FrequencyBinList): Float {
        val peakMagnitudes = getPeakMagnitudes(frequencyBins)
        val averagePeakMagnitude = peakMagnitudes.average()
        return averagePeakMagnitude.toFloat()
    }

    private fun getPeakMagnitudes(frequencyBins: FrequencyBinList): List<Float> {
        val sortedMagnitudes = getSortedMagnitudes(frequencyBins)
        return sortedMagnitudes.subList(0, numberOfPeaksToUse)
    }

    private fun getSortedMagnitudes(frequencyBins: FrequencyBinList): List<Float> {
        val magnitudes = getMagnitudes(frequencyBins)
        return magnitudes.sortedDescending()
    }

    private fun getMagnitudes(frequencyBins: FrequencyBinList): List<Float> {
        return frequencyBins.map { it.magnitude }
    }

}

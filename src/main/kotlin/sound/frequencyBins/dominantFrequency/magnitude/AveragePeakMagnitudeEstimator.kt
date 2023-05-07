package sound.frequencyBins.dominantFrequency.magnitude

import config.Config
import config.ConfigSingleton
import sound.frequencyBins.FrequencyBinList

class AveragePeakMagnitudeEstimator(
    private val config: Config = ConfigSingleton
) : MagnitudeEstimator {

    private val numberOfPeaksToUse: Int = config.magnitudeEstimationStrategy.numberOfPeaksToUse

    override fun estimate(frequency: Float, frequencyBins: FrequencyBinList): Float? {
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

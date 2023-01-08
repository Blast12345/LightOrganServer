package color

import color.frequenyFinders.MaximumFrequencyFinder
import color.frequenyFinders.MaximumFrequencyFinderInterface
import color.frequenyFinders.MinimumFrequencyFinder
import color.frequenyFinders.MinimumFrequencyFinderInterface
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList

interface HueFactoryInterface {
    fun create(dominantBin: FrequencyBin, frequencyBins: FrequencyBinList): Float?
}

class HueFactory(
    private val minimumFrequencyFinder: MinimumFrequencyFinderInterface = MinimumFrequencyFinder(),
    private val maximumFrequencyFinder: MaximumFrequencyFinderInterface = MaximumFrequencyFinder()
) : HueFactoryInterface {

    override fun create(dominantBin: FrequencyBin, frequencyBins: FrequencyBinList): Float? {
        return getHue(
            averageFrequency = getDominantFrequency(dominantBin),
            minimumFrequency = getMinimumFrequency(frequencyBins),
            maximumFrequency = getMaximumFrequency(frequencyBins)
        )
    }

    private fun getDominantFrequency(dominantBin: FrequencyBin): Float {
        return dominantBin.frequency
    }

    private fun getMinimumFrequency(frequencyBins: FrequencyBinList): Float? {
        return minimumFrequencyFinder.find(frequencyBins)
    }

    private fun getMaximumFrequency(frequencyBins: FrequencyBinList): Float? {
        return maximumFrequencyFinder.find(frequencyBins)
    }

    private fun getHue(
        averageFrequency: Float,
        minimumFrequency: Float?,
        maximumFrequency: Float?
    ): Float? {
        return if (minimumFrequency != null && maximumFrequency != null) {
            getHue(averageFrequency, minimumFrequency, maximumFrequency)
        } else {
            null
        }
    }

    private fun getHue(
        averageFrequency: Float,
        minimumFrequency: Float,
        maximumFrequency: Float
    ): Float? {
        return if (maximumFrequency == 0F) {
            null
        } else {
            val adjustedAverage = averageFrequency - minimumFrequency
            val range = maximumFrequency - minimumFrequency
            getHue(adjustedAverage, range)
        }
    }

    private fun getHue(adjustedAverage: Float, range: Float): Float {
        return adjustedAverage / range
    }

}
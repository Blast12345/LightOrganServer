package color

import sound.frequencyBins.FrequencyBinList

interface HueFactoryInterface {
    fun create(frequencyBins: FrequencyBinList): Float?
}

class HueFactory(
    private val averageFrequencyCalculator: AverageFrequencyCalculatorInterface = AverageFrequencyCalculator(),
    private val minimumFrequencyFinder: MinimumFrequencyFinderInterface = MinimumFrequencyFinder(),
    private val maximumFrequencyFinder: MaximumFrequencyFinderInterface = MaximumFrequencyFinder()
) : HueFactoryInterface {

    override fun create(frequencyBins: FrequencyBinList): Float? {
        val bassBins = getBassBins(frequencyBins)
        // TODO: Given the poor frequency resolution, average is not the best algorithm.
        // Try peak frequency or increasing the magnitudes by some power.
        val averageFrequency = averageFrequencyCalculator.calculate(bassBins)
        val minimumFrequency = minimumFrequencyFinder.find(bassBins)
        val maximumFrequency = maximumFrequencyFinder.find(bassBins)
        return getHue(averageFrequency, minimumFrequency, maximumFrequency)
    }

    private fun getBassBins(frequencyBins: FrequencyBinList): FrequencyBinList {
        // TODO: Create a BassBinsFilter
        return frequencyBins.filter { it.frequency < 120F }
    }

    private fun getHue(averageFrequency: Float?, minimumFrequency: Float?, maximumFrequency: Float?): Float? {
        return if (averageFrequency != null && minimumFrequency != null && maximumFrequency != null) {
            getHue(averageFrequency, minimumFrequency, maximumFrequency)
        } else {
            null
        }
    }

    private fun getHue(averageFrequency: Float, minimumFrequency: Float, maximumFrequency: Float): Float? {
        return if (maximumFrequency == 0F) {
            null
        } else {
            val range = maximumFrequency - minimumFrequency
            val adjustedAverage = averageFrequency - minimumFrequency
            getHue(range, adjustedAverage)
        }
    }

    private fun getHue(range: Float, adjustedAverage: Float): Float {
        return adjustedAverage / range
    }

}
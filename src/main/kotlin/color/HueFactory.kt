package color

import sound.frequencyBins.*

interface HueFactoryInterface {
    fun create(frequencyBinList: FrequencyBinList): Float?
}

class HueFactory(
    private val averageFrequencyCalculator: AverageFrequencyCalculatorInterface = AverageFrequencyCalculator(),
    private val minimumFrequencyFinder: MinimumFrequencyFinderInterface = MinimumFrequencyFinder(),
    private val maximumFrequencyFinder: MaximumFrequencyFinderInterface = MaximumFrequencyFinder()
) : HueFactoryInterface {

    override fun create(frequencyBinList: FrequencyBinList): Float? {
        val bassBins = getBassBins(frequencyBinList)
        val averageFrequency = averageFrequencyCalculator.calculate(bassBins)
        val minimumFrequency = minimumFrequencyFinder.find(bassBins)
        val maximumFrequency = maximumFrequencyFinder.find(bassBins)
        return getHue(averageFrequency, minimumFrequency, maximumFrequency)
    }

    private fun getBassBins(frequencyBinList: FrequencyBinList): FrequencyBinList {
        return frequencyBinList.filter { it.frequency < 120F }
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
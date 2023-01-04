package color

import sound.frequencyBins.*

interface HueFactoryInterface {
    fun create(frequencyBins: List<FrequencyBin>): Float?
}

class HueFactory(
    private val averageFrequencyCalculator: AverageFrequencyCalculatorInterface = AverageFrequencyCalculator(),
    private val minimumFrequencyFinder: MinimumFrequencyFinderInterface = MinimumFrequencyFinder(),
    private val maximumFrequencyFinder: MaximumFrequencyFinderInterface = MaximumFrequencyFinder()
) : HueFactoryInterface {

    override fun create(frequencyBins: List<FrequencyBin>): Float? {
        val averageFrequency = averageFrequencyCalculator.calculate(frequencyBins)
        val minimumFrequency = minimumFrequencyFinder.find(frequencyBins)
        val maximumFrequency = maximumFrequencyFinder.find(frequencyBins)
        return getHue(averageFrequency, minimumFrequency, maximumFrequency)
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
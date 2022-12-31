package colorListener.color

import sound.frequencyBins.*

interface HueFactoryInterface {
    fun hueFrom(frequencyBins: List<FrequencyBin>): Float?
}

class HueFactory(
    private val averageFrequencyFactory: AverageFrequencyFactoryInterface = AverageFrequencyFactory(),
    private val minimumFrequencyFactory: MinimumFrequencyFactoryInterface = MinimumFrequencyFactory(),
    private val maximumFrequencyFactory: MaximumFrequencyFactoryInterface = MaximumFrequencyFactory()
) : HueFactoryInterface {

    override fun hueFrom(frequencyBins: List<FrequencyBin>): Float? {
        val averageFrequency = averageFrequencyFactory.averageFrequencyFrom(frequencyBins)
        val minimumFrequency = minimumFrequencyFactory.minimumFrequencyFrom(frequencyBins)
        val maximumFrequency = maximumFrequencyFactory.maximumFrequencyFrom(frequencyBins)

        return if (averageFrequency != null && minimumFrequency != null && maximumFrequency != null) {
            hue(averageFrequency, minimumFrequency, maximumFrequency)
        } else {
            null
        }
    }

    private fun hue(averageFrequency: Float, minimumFrequency: Float, maximumFrequency: Float): Float? {
        return if (maximumFrequency == 0F) {
            null
        } else {
            (averageFrequency - minimumFrequency) / maximumFrequency
        }
    }

}
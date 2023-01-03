package color

import sound.frequencyBins.*

interface HueFactoryInterface {
    fun create(frequencyBins: List<FrequencyBin>): Float?
}

class HueFactory(
    private val averageFrequencyFactory: AverageFrequencyFactoryInterface = AverageFrequencyFactory(),
    private val minimumFrequencyFactory: MinimumFrequencyFactoryInterface = MinimumFrequencyFactory(),
    private val maximumFrequencyFactory: MaximumFrequencyFactoryInterface = MaximumFrequencyFactory()
) : HueFactoryInterface {

    override fun create(frequencyBins: List<FrequencyBin>): Float? {
        val averageFrequency = averageFrequencyFactory.averageFrequency(frequencyBins)
        val minimumFrequency = minimumFrequencyFactory.minimumFrequency(frequencyBins)
        val maximumFrequency = maximumFrequencyFactory.maximumFrequency(frequencyBins)

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
            (averageFrequency - minimumFrequency) / maximumFrequency
        }
    }

}
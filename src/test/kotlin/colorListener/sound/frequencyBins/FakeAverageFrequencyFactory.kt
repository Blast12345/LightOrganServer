package colorListener.sound.frequencyBins

class FakeAverageFrequencyFactory : AverageFrequencyFactoryInterface {

    var frequencyBins: FrequencyBins? = null
    var frequency: Float? = 1F

    override fun averageFrequencyFrom(frequencyBins: FrequencyBins): Float? {
        this.frequencyBins = frequencyBins
        return frequency
    }

}
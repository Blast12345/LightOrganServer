package colorListener.sound.frequencyBins

class FakeMaximumFrequencyFactory : MaximumFrequencyFactoryInterface {

    var frequencyBins: FrequencyBins? = null
    var frequency: Float? = 1F

    override fun maximumFrequencyFrom(frequencyBins: FrequencyBins): Float? {
        this.frequencyBins = frequencyBins
        return frequency
    }

}
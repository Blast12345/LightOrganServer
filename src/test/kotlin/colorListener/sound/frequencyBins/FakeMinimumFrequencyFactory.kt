package colorListener.sound.frequencyBins

class FakeMinimumFrequencyFactory : MinimumFrequencyFactoryInterface {

    var frequencyBins: FrequencyBins? = null
    var frequency: Float? = 1F

    override fun minimumFrequencyFrom(frequencyBins: FrequencyBins): Float? {
        this.frequencyBins = frequencyBins
        return frequency
    }

}
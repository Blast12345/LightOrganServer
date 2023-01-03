package sound.frequencyBins

class FakeMaximumFrequencyFactory : MaximumFrequencyFactoryInterface {

    var frequencyBins: FrequencyBins? = null
    var frequency: Float? = 1F

    override fun maximumFrequency(frequencyBins: FrequencyBins): Float? {
        this.frequencyBins = frequencyBins
        return frequency
    }

}
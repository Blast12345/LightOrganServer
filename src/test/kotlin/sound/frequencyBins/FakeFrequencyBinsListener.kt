package sound.frequencyBins

class FakeFrequencyBinsListener : FrequencyBinsListenerInterface {

    var nextFrequencyBins: NextFrequencyBins? = null

    override fun listenForFrequencyBins(lambda: NextFrequencyBins) {
        this.nextFrequencyBins = lambda
    }

}
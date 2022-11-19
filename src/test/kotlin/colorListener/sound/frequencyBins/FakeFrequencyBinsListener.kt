package colorListener.sound.frequencyBins

class FakeFrequencyBinsListener : FrequencyBinsListenerInterface {

    var lambda: NextFrequencyBins? = null

    override fun listenForFrequencyBins(lambda: NextFrequencyBins) {
        this.lambda = lambda
    }

}
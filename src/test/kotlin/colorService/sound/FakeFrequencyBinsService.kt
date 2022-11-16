package colorService.sound

class FakeFrequencyBinsService: FrequencyBinsServiceInterface {

    var lambda: NextFrequencyBins? = null

    override fun listenForFrequencyBins(lambda: NextFrequencyBins) {
        this.lambda = lambda
    }

}
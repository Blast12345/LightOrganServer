package colorService.sound

class FakeFrequencyBinService: FrequencyBinServiceInterface {

    var lambda: NextFrequencyBins? = null

    override fun listenForFrequencyBins(lambda: NextFrequencyBins) {
        this.lambda = lambda
    }

}
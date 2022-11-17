package colorListener.sound

class FakeFftService: FftServiceInterface {

    var lambda: NextFftData? = null

    override fun listenForFftData(lambda: NextFftData) {
        this.lambda = lambda
    }

}
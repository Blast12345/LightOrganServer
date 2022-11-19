package colorListener.sound.fft

import colorListener.sound.FftServiceInterface
import colorListener.sound.NextFftData

class FakeFftService : FftServiceInterface {

    var lambda: NextFftData? = null

    override fun listenForFftData(lambda: NextFftData) {
        this.lambda = lambda
    }

}
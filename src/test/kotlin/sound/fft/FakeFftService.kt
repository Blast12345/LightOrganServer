package sound.fft

import sound.fft.FftServiceInterface
import sound.fft.NextFftData

class FakeFftService : FftServiceInterface {

    var nextFftData: NextFftData? = null

    override fun listenForFftData(lambda: NextFftData) {
        this.nextFftData = lambda
    }

}
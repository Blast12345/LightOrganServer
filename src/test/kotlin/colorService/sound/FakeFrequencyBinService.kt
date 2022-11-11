package colorService.sound

import colorService.color.FrequencyBinServiceInterface
import colorService.color.NextFrequencyBins

class FakeFrequencyBinService: FrequencyBinServiceInterface {

    var lambda: NextFrequencyBins? = null

    override fun listenForFrequencyBins(lambda: NextFrequencyBins) {
        this.lambda = lambda
    }

}
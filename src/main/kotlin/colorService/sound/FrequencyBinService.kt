package colorService.sound

import colorService.sound.FrequencyBin

typealias NextFrequencyBins = (List<FrequencyBin>) -> Unit

interface FrequencyBinServiceInterface {
    fun listenForFrequencyBins(lambda: NextFrequencyBins)
}

class FrequencyBinService: FrequencyBinServiceInterface {

    override fun listenForFrequencyBins(lambda: NextFrequencyBins) {
        // TODO: Implement me
        while (true) {
            lambda(listOf(FrequencyBin(0.0, 0.0)))
        }
    }
}
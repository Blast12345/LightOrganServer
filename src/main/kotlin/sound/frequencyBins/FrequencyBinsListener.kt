package sound.frequencyBins

import sound.fft.FftService
import sound.fft.FftServiceInterface

typealias NextFrequencyBins = (List<FrequencyBin>) -> Unit

interface FrequencyBinsListenerInterface {
    fun listenForFrequencyBins(lambda: NextFrequencyBins)
}

class FrequencyBinsListener(
    private val fftService: FftServiceInterface = FftService(),
    private val frequencyBinsFactory: FrequencyBinsFactoryInterface = FrequencyBinsFactory()
) : FrequencyBinsListenerInterface {

    override fun listenForFrequencyBins(lambda: NextFrequencyBins) {
        fftService.listenForFftData { sampleRate, sampleSize, amplitudes ->
            val frequencyBins = frequencyBinsFactory.createFrom(sampleRate, sampleSize, amplitudes)
            lambda(frequencyBins)
        }
    }

}
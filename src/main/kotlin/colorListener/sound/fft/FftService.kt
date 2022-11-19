package colorListener.sound

import colorListener.sound.fft.AmplitudeFactory
import colorListener.sound.fft.AmplitudeFactoryInterface
import colorListener.sound.input.LineInputListener
import colorListener.sound.input.LineInputListenerInterface

typealias NextFftData = (sampleRate: Int, sampleSize: Int, amplitudes: DoubleArray) -> Unit

interface FftServiceInterface {
    fun listenForFftData(lambda: NextFftData)
}

class FftService(
    private val lineInputListener: LineInputListenerInterface = LineInputListener(),
    private val amplitudeFactory: AmplitudeFactoryInterface = AmplitudeFactory()
) : FftServiceInterface {

    override fun listenForFftData(lambda: NextFftData) {
        lineInputListener.listenForNextAudioSample { sampleRate, sampleSize, signal ->
            val amplitudes = amplitudeFactory.createFrom(signal)
            lambda(sampleRate, sampleSize, amplitudes)
        }
    }
}


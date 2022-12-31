package sound.fft

typealias NextFftData = (sampleRate: Int, sampleSize: Int, amplitudes: DoubleArray) -> Unit

interface FftServiceInterface {
    fun listenForFftData(lambda: NextFftData)
}

class FftService(
//    private val lineInputListener: LineInputListenerInterface = LineInputListener(),
    private val amplitudeFactory: AmplitudeFactoryInterface = AmplitudeFactory()
) : FftServiceInterface {

    override fun listenForFftData(lambda: NextFftData) {
        TODO("Reimplement")
//        lineInputListener.listenForNextAudioSample { sampleRate, sampleSize, signal ->
//            val amplitudes = amplitudeFactory.createFrom(signal)
//            lambda(sampleRate, sampleSize, amplitudes)
//        }
    }
}


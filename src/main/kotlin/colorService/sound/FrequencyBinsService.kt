package colorService.sound

typealias NextFrequencyBins = (List<FrequencyBin>) -> Unit

interface FrequencyBinsServiceInterface {
    fun listenForFrequencyBins(lambda: NextFrequencyBins)
}

class FrequencyBinsService(private val fftService: FftServiceInterface = FftService(),
                           private val frequencyBinsFactory: FrequencyBinsFactoryInterface = FrequencyBinsFactory()): FrequencyBinsServiceInterface {

    override fun listenForFrequencyBins(lambda: NextFrequencyBins) {
        fftService.listenForFftData { sampleRate, sampleSize, amplitudes ->
            val frequencyBins = frequencyBinsFactory.createFrom(sampleRate, sampleSize, amplitudes)
            lambda(frequencyBins)
        }
    }

}
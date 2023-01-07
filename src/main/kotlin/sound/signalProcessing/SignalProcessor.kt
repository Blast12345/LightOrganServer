package sound.signalProcessing

import sound.input.samples.AudioSignal

interface SignalProcessorInterface {
    fun process(audioSignal: AudioSignal, lowestFrequency: Float): DoubleArray
}

// TODO: This name feels fishy
class SignalProcessor(
    private val sampleExtractor: SampleExtractorInterface = SampleExtractor(),
    private val interpolator: InterpolatorInterface = Interpolator(),
    private val hannFilter: HannFilterInterface = HannFilter(),
    private val hannFilterCorrector: HannFilterCorrectorInterface = HannFilterCorrector(),
    private val fftAlgorithm: FftAlgorithmInterface = FftAlgorithm()
) : SignalProcessorInterface {

    override fun process(audioSignal: AudioSignal, lowestFrequency: Float): DoubleArray {
        val fewestSamplesNeeded = sampleExtractor.extract(audioSignal, lowestFrequency)
        val interpolatedSamples = interpolator.interpolate(fewestSamplesNeeded, audioSignal.sampleRate.toInt())
        val filteredSignal = hannFilter.filter(interpolatedSamples)
        val correctedSignal = hannFilterCorrector.correct(filteredSignal)
        return fftAlgorithm.calculateMagnitudes(correctedSignal)
    }

}
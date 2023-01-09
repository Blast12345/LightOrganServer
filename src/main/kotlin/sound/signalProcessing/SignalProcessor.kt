package sound.signalProcessing

import sound.input.samples.AudioSignal
import sound.signalProcessing.hannFilter.HannFilterInterface
import sound.signalProcessing.hannFilter.NormalizedHannFilter
import sound.signalProcessing.interpolator.InterpolatorInterface
import sound.signalProcessing.interpolator.NormalizedInterpolator

interface SignalProcessorInterface {
    fun process(audioSignal: AudioSignal, lowestFrequency: Float): DoubleArray
}

class SignalProcessor(
    private val sampleExtractor: SampleExtractorInterface = SampleExtractor(),
    private val hannFilter: HannFilterInterface = NormalizedHannFilter(),
    private val interpolator: InterpolatorInterface = NormalizedInterpolator()
) : SignalProcessorInterface {

    override fun process(audioSignal: AudioSignal, lowestFrequency: Float): DoubleArray {
        val fewestSamplesNeeded = sampleExtractor.extract(audioSignal, lowestFrequency)
        val filteredSamples = hannFilter.filter(fewestSamplesNeeded)
        // TODO: Interpolate to power of 2 (e.g. 65,536)
        return interpolator.interpolate(filteredSamples, audioSignal.sampleRate.toInt())
    }

}
package sound.signalProcessing

import Config
import sound.input.samples.AudioSignal
import sound.signalProcessing.hannFilter.HannFilterInterface
import sound.signalProcessing.hannFilter.NormalizedHannFilter
import sound.signalProcessing.zeroPaddingInterpolator.NormalizedZeroPaddingInterpolator
import sound.signalProcessing.zeroPaddingInterpolator.ZeroPaddingInterpolatorInterface

interface SignalProcessorInterface {
    fun process(audioSignal: AudioSignal): DoubleArray
}

class SignalProcessor(
    private val sampleSize: Int = Config().sampleSize,
    private val sampleExtractor: SampleExtractorInterface = SampleExtractor(),
    private val hannFilter: HannFilterInterface = NormalizedHannFilter(),
    private val interpolator: ZeroPaddingInterpolatorInterface = NormalizedZeroPaddingInterpolator()
) : SignalProcessorInterface {

    override fun process(audioSignal: AudioSignal): DoubleArray {
        val fewestSamplesNeeded = sampleExtractor.extract(audioSignal, sampleSize)
        val filteredSamples = hannFilter.filter(fewestSamplesNeeded)
        // TODO: Interpolate to power of 2 (e.g. 65,536)
        return interpolator.interpolate(filteredSamples, audioSignal.sampleRate.toInt())
    }

}
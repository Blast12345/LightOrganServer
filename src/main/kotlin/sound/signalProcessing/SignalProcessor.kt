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
    private val interpolator: ZeroPaddingInterpolatorInterface = NormalizedZeroPaddingInterpolator(),
    private val interpolatedSampleSize: Int = Config().interpolatedSampleSize
) : SignalProcessorInterface {

    override fun process(audioSignal: AudioSignal): DoubleArray {
        val extractedSamples = sampleExtractor.extract(audioSignal, sampleSize)
        val filteredSamples = hannFilter.filter(extractedSamples)
        return interpolator.interpolate(filteredSamples, interpolatedSampleSize)
    }

}
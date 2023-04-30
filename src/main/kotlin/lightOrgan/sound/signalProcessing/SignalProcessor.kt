package sound.signalProcessing

import config.PersistedConfig
import input.audioFrame.AudioFrame
import lightOrgan.sound.SampleExtractor
import lightOrgan.sound.SampleExtractorInterface
import lightOrgan.sound.signalProcessing.hannFilter.HannFilterInterface
import lightOrgan.sound.signalProcessing.hannFilter.NormalizedHannFilter
import lightOrgan.sound.signalProcessing.zeroPaddingInterpolator.NormalizedZeroPaddingInterpolator
import lightOrgan.sound.signalProcessing.zeroPaddingInterpolator.ZeroPaddingInterpolatorInterface

interface SignalProcessorInterface {
    fun process(audioFrame: AudioFrame): DoubleArray
}

class SignalProcessor(
    private val sampleSize: Int = PersistedConfig().sampleSize,
    private val interpolatedSampleSize: Int = PersistedConfig().interpolatedSampleSize,
    private val sampleExtractor: SampleExtractorInterface = SampleExtractor(),
    private val hannFilter: HannFilterInterface = NormalizedHannFilter(),
    private val interpolator: ZeroPaddingInterpolatorInterface = NormalizedZeroPaddingInterpolator()
) : SignalProcessorInterface {

    override fun process(audioFrame: AudioFrame): DoubleArray {
        val extractedSamples = sampleExtractor.extract(audioFrame, sampleSize)
        val filteredSamples = hannFilter.filter(extractedSamples)
        return interpolator.interpolate(filteredSamples, interpolatedSampleSize)
    }

}
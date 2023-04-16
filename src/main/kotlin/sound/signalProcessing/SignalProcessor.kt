package sound.signalProcessing

import config.Config
import config.ConfigSingleton
import input.audioFrame.AudioFrame
import sound.signalProcessing.hannFilter.HannFilterInterface
import sound.signalProcessing.hannFilter.NormalizedHannFilter
import sound.signalProcessing.zeroPaddingInterpolator.NormalizedZeroPaddingInterpolator
import sound.signalProcessing.zeroPaddingInterpolator.ZeroPaddingInterpolatorInterface

interface SignalProcessorInterface {
    fun process(audioFrame: AudioFrame): DoubleArray
}

class SignalProcessor(
    private val config: Config = ConfigSingleton,
    private val sampleExtractor: SampleExtractorInterface = SampleExtractor(),
    private val hannFilter: HannFilterInterface = NormalizedHannFilter(),
    private val interpolator: ZeroPaddingInterpolatorInterface = NormalizedZeroPaddingInterpolator()
) : SignalProcessorInterface {

    private val sampleSize = config.sampleSize
    private val interpolatedSampleSize: Int = config.interpolatedSampleSize

    override fun process(audioFrame: AudioFrame): DoubleArray {
        val extractedSamples = sampleExtractor.extract(audioFrame, sampleSize)
        val filteredSamples = hannFilter.filter(extractedSamples)
        return interpolator.interpolate(filteredSamples, interpolatedSampleSize)
    }

}
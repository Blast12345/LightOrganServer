package lightOrgan.sound.signalProcessing

import config.Config
import config.ConfigSingleton
import input.samples.AudioSignal
import lightOrgan.sound.signalProcessing.hannFilter.HannFilterInterface
import lightOrgan.sound.signalProcessing.hannFilter.NormalizedHannFilter
import lightOrgan.sound.signalProcessing.zeroPaddingInterpolator.NormalizedZeroPaddingInterpolator
import lightOrgan.sound.signalProcessing.zeroPaddingInterpolator.ZeroPaddingInterpolatorInterface

interface SignalProcessorInterface {
    fun process(audioSignal: AudioSignal): DoubleArray
}

class SignalProcessor(
    private val config: Config = ConfigSingleton,
    private val sampleExtractor: SampleExtractorInterface = SampleExtractor(),
    private val hannFilter: HannFilterInterface = NormalizedHannFilter(),
    private val interpolator: ZeroPaddingInterpolatorInterface = NormalizedZeroPaddingInterpolator()
) : SignalProcessorInterface {

    private val sampleSize = config.sampleSize
    private val interpolatedSampleSize: Int = config.interpolatedSampleSize

    override fun process(audioSignal: AudioSignal): DoubleArray {
        val extractedSamples = sampleExtractor.extract(audioSignal, sampleSize)
        val filteredSamples = hannFilter.filter(extractedSamples)
        return interpolator.interpolate(filteredSamples, interpolatedSampleSize)
    }

}
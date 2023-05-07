package sound.signalProcessing

import config.Config
import config.ConfigProvider
import input.audioFrame.AudioFrame
import sound.signalProcessing.hannFilter.HannFilter
import sound.signalProcessing.hannFilter.NormalizedHannFilter
import sound.signalProcessing.zeroPaddingInterpolator.NormalizedZeroPaddingInterpolator
import sound.signalProcessing.zeroPaddingInterpolator.ZeroPaddingInterpolator

class SignalProcessor(
    private val config: Config = ConfigProvider().current,
    private val sampleExtractor: SampleExtractor = SampleExtractor(),
    private val hannFilter: HannFilter = NormalizedHannFilter(),
    private val interpolator: ZeroPaddingInterpolator = NormalizedZeroPaddingInterpolator()
) {

    fun process(audioFrame: AudioFrame): DoubleArray {
        val extractedSamples = sampleExtractor.extract(audioFrame, sampleSize)
        val filteredSamples = hannFilter.filter(extractedSamples)
        return interpolator.interpolate(filteredSamples, interpolatedSampleSize)
    }

    private val sampleSize: Int
        get() = config.sampleSize

    private val interpolatedSampleSize: Int
        get() = config.interpolatedSampleSize

}
package lightOrgan.spectrum

import audio.samples.AudioFrame
import dsp.filtering.HighPassFilter
import dsp.filtering.LowPassFilter
import dsp.filtering.config.FilterBuilder
import dsp.filtering.config.FilterConfig

// TODO: Magnitude typealias with toDBFS() and vice versa dbfs.toMagnitude()
// ENHANCEMENT: Show the filter response in the UI
// ENHANCEMENT: Make configs configurable via the UI, then automatically rebuild filters
class FilterManager(
    private val highPassConfig: FilterConfig.HighPass?,
    private val lowPassConfig: FilterConfig.LowPass?,
    private val filterBuilder: FilterBuilder = FilterBuilder(),
) {

    private var highPassFilter: HighPassFilter? = null
    private var lowPassFilter: LowPassFilter? = null
    private var sampleRate: Float? = null

    fun filter(audio: AudioFrame): AudioFrame {
        rebuildIfNeeded(audio.format.sampleRate)

        var samples = audio.samples

        highPassFilter?.let { samples = it.filter(samples) }
        lowPassFilter?.let { samples = it.filter(samples) }

        return AudioFrame(samples, audio.format)
    }

    fun highPassThresholdFrequency(thresholdDb: Float): Float? {
        return highPassConfig?.frequencyAtMagnitude(thresholdDb)
    }

    fun lowPassThresholdFrequency(thresholdDb: Float): Float? {
        return lowPassConfig?.frequencyAtMagnitude(thresholdDb)
    }

    private fun rebuildIfNeeded(sampleRate: Float) {
        if (sampleRate == this.sampleRate) return

        this.sampleRate = sampleRate
        highPassFilter = highPassConfig?.let { filterBuilder.build(it, sampleRate) }
        lowPassFilter = lowPassConfig?.let { filterBuilder.build(it, sampleRate) }
    }

}
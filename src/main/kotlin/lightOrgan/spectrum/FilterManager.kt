package lightOrgan.spectrum

import audio.samples.AudioFrame
import dsp.filtering.HighPassFilter
import dsp.filtering.LowPassFilter
import dsp.filtering.config.FilterBuilder
import dsp.filtering.config.FilterConfig
import kotlin.math.pow

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

    fun highestPassingFrequency(
        sampleRate: Float,
        thresholdDb: Float,
        precisionHz: Float = 1f
    ): Float? {
        val filter = lowPassFilter ?: return null
        rebuildIfNeeded(sampleRate)

        val threshold = 10.0.pow(thresholdDb / 20.0).toFloat()
        val nyquist = sampleRate / 2f
        val cutoff = filter.cutoffFrequency

        var low = cutoff
        var high = nyquist

        while (high - low > precisionHz) {
            val mid = (low + high) / 2f
            if (filter.magnitudeAt(mid) > threshold) {
                low = mid
            } else {
                high = mid
            }
        }

        return low
    }

    private fun rebuildIfNeeded(sampleRate: Float) {
        if (sampleRate == this.sampleRate) return

        this.sampleRate = sampleRate
        highPassFilter = highPassConfig?.let { filterBuilder.build(it, sampleRate) }
        lowPassFilter = lowPassConfig?.let { filterBuilder.build(it, sampleRate) }
    }

}

class Downsampler {

    private var remainder: Int = 0

    fun decimate(audio: AudioFrame, targetFrequency: Float): AudioFrame {
        val factor = (audio.format.sampleRate / (2 * targetFrequency)).toInt()

        if (factor <= 1) return audio

        val samples = audio.samples
        val outputSize = if (remainder >= samples.size) 0
        else (samples.size - remainder + factor - 1) / factor

        val decimatedSamples = FloatArray(outputSize) { index ->
            samples[remainder + index * factor]
        }

        remainder = remainder + outputSize * factor - samples.size

        val decimatedFormat = audio.format.copy(sampleRate = audio.format.sampleRate / factor)

        return AudioFrame(decimatedSamples, decimatedFormat)
    }

    fun reset() {
        remainder = 0
    }

}
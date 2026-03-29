package lightOrgan.spectrum

import audio.samples.AudioFrame
import audio.samples.RollingAudioBuffer
import config.ConfigSingleton
import dsp.MonoMixer
import dsp.ZeroPaddingInterpolator
import dsp.fft.FrequencyBins
import dsp.fft.FrequencyBinsCalculator
import dsp.filtering.OrderedFilter
import dsp.filtering.config.FilterBuilder
import dsp.windowing.HannWindow
import dsp.windowing.WindowFunction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ENHANCEMENT: If implementing other calculation strategies (e.g. DFT, CZT), then create a bin calculator interface
// ENHANCEMENT: Make scaling configurable
// ENHANCEMENT: Improve handling of discontinuities (though I have doubt it is possible)
class SpectrumManager(
    private val config: SpectrumConfig = ConfigSingleton.spectrum,
    private val monoMixer: MonoMixer = MonoMixer(),
    private val filterBuilder: FilterBuilder = FilterBuilder(),
    private val audioBuffer: RollingAudioBuffer = RollingAudioBuffer(config.sampleSize),
    private val windowFunction: WindowFunction = HannWindow(),
    private val interpolator: ZeroPaddingInterpolator = ZeroPaddingInterpolator(),
    private val frequencyBinsCalculator: FrequencyBinsCalculator = FrequencyBinsCalculator()
) {

    private var highPassFilter: OrderedFilter? = null
    private var lowPassFilter: OrderedFilter? = null

    private val _frequencyBins = MutableStateFlow<FrequencyBins>(emptyList())
    val frequencyBins: StateFlow<FrequencyBins> = _frequencyBins.asStateFlow()

    fun calculate(audio: AudioFrame): FrequencyBins {
        // Signal Processing
        rebuildFiltersIfNeeded(audio.format.sampleRate)

        var processedAudio = monoMixer.mix(audio)
        processedAudio = highPassFilter?.filter(processedAudio) ?: processedAudio
        processedAudio = lowPassFilter?.filter(processedAudio) ?: processedAudio

        // Frame preparation
        var preparedFrame = audioBuffer.append(processedAudio)
        preparedFrame = windowFunction.appliedTo(preparedFrame)
        preparedFrame = interpolator.interpolate(preparedFrame)

        // Bin generation
        var allBins = frequencyBinsCalculator.calculate(preparedFrame)
        allBins = applyWindowCorrection(allBins)

        // Return
        _frequencyBins.value = allBins
        return allBins
    }

    private fun rebuildFiltersIfNeeded(sampleRate: Float) {
        if (config.highPassFilter != null && highPassFilter?.sampleRate != sampleRate) {
            highPassFilter = filterBuilder.build(config.highPassFilter, sampleRate)
        }

        if (config.lowPassFilter != null && lowPassFilter?.sampleRate != sampleRate) {
            lowPassFilter = filterBuilder.build(config.lowPassFilter, sampleRate)
        }
    }

    private fun OrderedFilter.filter(audio: AudioFrame): AudioFrame {
        return AudioFrame(filter(audio.samples), audio.format)
    }

    private fun WindowFunction.appliedTo(audio: AudioFrame): AudioFrame {
        return AudioFrame(appliedTo(audio.samples), audio.format)
    }

    private fun ZeroPaddingInterpolator.interpolate(audio: AudioFrame): AudioFrame {
        return AudioFrame(interpolate(audio.samples, config.interpolatedSampleSize), audio.format)
    }

    private fun applyWindowCorrection(bins: FrequencyBins): FrequencyBins {
        return bins.map { it.copy(magnitude = it.magnitude * windowFunction.amplitudeCorrectionFactor) }
    }

}
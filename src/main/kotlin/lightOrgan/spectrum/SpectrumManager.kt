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
import dsp.windowing.Window
import extensions.inSeconds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import math.nextPowerOfTwo

// ENHANCEMENT: If implementing other calculation strategies (e.g. DFT, CZT), then create a bin calculator interface
// ENHANCEMENT: Make scaling configurable
// ENHANCEMENT: Improve handling of discontinuities (though I have doubt it is possible)
class SpectrumManager(
    private val config: SpectrumConfig = ConfigSingleton.spectrum,
    private val monoMixer: MonoMixer = MonoMixer(),
    private val filterBuilder: FilterBuilder = FilterBuilder(),
    private val audioBuffer: RollingAudioBuffer = RollingAudioBuffer(),
    private val window: Window = config.window.createWindow(),
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
        val preparedFrame = prepareFrame(processedAudio)

        // Bin generation
        val allBins = frequencyBinsCalculator.calculate(preparedFrame.audio)
        val correctedBins = allBins.map { it.copy(magnitude = it.magnitude * preparedFrame.magnitudeCorrectionFactor) }

        // Return
        _frequencyBins.value = correctedBins
        return correctedBins
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

    // Frame Prep
    private fun prepareFrame(audio: AudioFrame): PreparedFrame {
        val sampleSize = (config.frameDuration.inSeconds * audio.format.sampleRate).toInt()
        val samplesSizeForDesiredSpacing = audio.format.sampleRate / config.approximateBinSpacing
        val optimalFftLength = nextPowerOfTwo(samplesSizeForDesiredSpacing.toInt())

        val preparedAudio = audio
            .let { updateBuffer(audio, sampleSize) }
            .let { applyWindowFunction(it) }
            .let { interpolate(it, optimalFftLength) }

        return PreparedFrame(
            audio = preparedAudio,
            magnitudeCorrectionFactor = window.magnitudeCorrectionFactor(sampleSize)
        )
    }

    private fun updateBuffer(frame: AudioFrame, requiredSize: Int): AudioFrame {
        audioBuffer.size = requiredSize
        return audioBuffer.append(frame)
    }

    private fun applyWindowFunction(audio: AudioFrame): AudioFrame {
        return AudioFrame(
            samples = window.appliedTo(audio.samples),
            format = audio.format
        )
    }

    private fun interpolate(audio: AudioFrame, targetSize: Int): AudioFrame {
        return AudioFrame(
            samples = interpolator.interpolate(audio.samples, targetSize),
            format = audio.format
        )
    }

    private data class PreparedFrame(
        val audio: AudioFrame,
        val magnitudeCorrectionFactor: Float
    )

}
package lightOrgan.spectrum

import audio.samples.AudioFrame
import audio.samples.RollingAudioBuffer
import config.ConfigSingleton
import dsp.MonoMixer
import dsp.ZeroPaddingInterpolator
import dsp.fft.FrequencyBins
import dsp.fft.FrequencyBinsCalculator
import dsp.filtering.SampleFilter
import dsp.filtering.config.FilterBuilder
import dsp.windowing.HannWindow
import dsp.windowing.WindowFunction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ENHANCEMENT: If implementing other calculation strategies (e.g. DFT, CZT), then create a bin calculator interface
// ENHANCEMENT: Make scaling configurable
class SpectrumManager(
    private val config: SpectrumConfig = ConfigSingleton.spectrum,
    private val monoMixer: MonoMixer = MonoMixer(),
    private val audioBuffer: RollingAudioBuffer = RollingAudioBuffer(config.sampleSize),
    private val windowFunction: WindowFunction = HannWindow(),
    private val interpolator: ZeroPaddingInterpolator = ZeroPaddingInterpolator(),
    private val frequencyBinsCalculator: FrequencyBinsCalculator = FrequencyBinsCalculator()
) {

    private var highPassFilter: SampleFilter? = null
    private var lowPassFilter: SampleFilter? = null

    private val _frequencyBins = MutableStateFlow<FrequencyBins>(emptyList())
    val frequencyBins: StateFlow<FrequencyBins> = _frequencyBins.asStateFlow()

    fun calculate(audio: AudioFrame): FrequencyBins {
        rebuildFiltersIfNeeded(audio.format.sampleRate)

        // Signal Processing
        val monoAudio = monoMixer.mix(audio)
        val filteredAudio1 = highPassFilter?.filter(monoAudio.samples) ?: monoAudio.samples
        val filteredAudio2 = lowPassFilter?.filter(filteredAudio1) ?: filteredAudio1

        val filteredFrame = AudioFrame(filteredAudio2, monoAudio.format)

        audioBuffer.append(filteredFrame)

        val bufferedAudio = audioBuffer.current ?: return _frequencyBins.value
        val windowedFrame = windowFunction.appliedTo(bufferedAudio.samples)
        val interpolatedFrame = interpolator.interpolate(windowedFrame, config.interpolatedSampleSize)

        // Bin generation
        val allBins = frequencyBinsCalculator
            .calculate(interpolatedFrame, monoAudio.format)
            .applyWindowCorrection()

        _frequencyBins.value = allBins

        return allBins
    }

    private fun rebuildFiltersIfNeeded(sampleRate: Float) {
        if (config.highPassFilter != null && highPassFilter?.sampleRate != sampleRate) {
            highPassFilter = FilterBuilder.build(config.highPassFilter, sampleRate)
        }

        if (config.lowPassFilter != null && lowPassFilter?.sampleRate != sampleRate) {
            lowPassFilter = FilterBuilder.build(config.lowPassFilter, sampleRate)
        }
    }

    private fun FrequencyBins.applyWindowCorrection(): FrequencyBins {
        return map { it.copy(magnitude = it.magnitude * windowFunction.amplitudeCorrectionFactor) }
    }

}
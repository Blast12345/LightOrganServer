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
import dsp.filtering.config.FilterConfig
import dsp.windowing.HannWindow
import dsp.windowing.WindowFunction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ENHANCEMENT: If implementing other calculation strategies (e.g. DFT, CZT), then create a bin calculator interface
// ENHANCEMENT: Make scaling configurable
class SpectrumManager(
    private val monoMixer: MonoMixer = MonoMixer(),
    private val highPassConfig: FilterConfig? = ConfigSingleton.highPassFilter,
    private val lowPassConfig: FilterConfig? = ConfigSingleton.lowPassFilter,
    private val audioBuffer: RollingAudioBuffer = RollingAudioBuffer(ConfigSingleton.sampleSize),
    private val windowFunction: WindowFunction = HannWindow(),
    private val interpolator: ZeroPaddingInterpolator = ZeroPaddingInterpolator(),
    private val frequencyBinsCalculator: FrequencyBinsCalculator = FrequencyBinsCalculator(),
    private val magnitudeMultiplier: Float = ConfigSingleton.magnitudeMultiplier
) {

    private var sampleRate: Float? = null
    private var highPassFilter: SampleFilter? = null
    private var lowPassFilter: SampleFilter? = null

    private val _frequencyBins = MutableStateFlow<FrequencyBins>(emptyList())
    val frequencyBins: StateFlow<FrequencyBins> = _frequencyBins.asStateFlow()

    fun calculate(audio: AudioFrame): FrequencyBins {
        if (audio.format.sampleRate != sampleRate) {
            sampleRate = audio.format.sampleRate
            audioBuffer.reset()
            rebuildFilters()
        }

        // Signal Processing
        val monoAudio = monoMixer.mix(audio)
        val filteredAudio1 = highPassFilter?.filter(monoAudio.samples) ?: monoAudio.samples
        val filteredAudio2 = lowPassFilter?.filter(filteredAudio1) ?: filteredAudio1

        val filteredFrame = AudioFrame(filteredAudio2, monoAudio.format)

        audioBuffer.append(filteredFrame)

        val bufferedAudio = audioBuffer.current ?: return _frequencyBins.value
        val windowedFrame = windowFunction.appliedTo(bufferedAudio.samples)
        val interpolatedFrame = interpolator.interpolate(windowedFrame)

        // Bin generation

        val allBins = frequencyBinsCalculator
            .calculate(interpolatedFrame, monoAudio.format)
            .applyWindowCorrection()
            .applyMagnitudeMultiplier()

        _frequencyBins.value = allBins

        return allBins
    }

    private fun rebuildFilters() {
        val rate = sampleRate ?: return
        highPassFilter = highPassConfig?.let { FilterBuilder.build(it, rate) }
        lowPassFilter = lowPassConfig?.let { FilterBuilder.build(it, rate) }
    }

    private fun FrequencyBins.applyWindowCorrection(): FrequencyBins {
        return map { it.copy(magnitude = it.magnitude * windowFunction.amplitudeCorrectionFactor) }
    }

    private fun FrequencyBins.applyMagnitudeMultiplier(): FrequencyBins {
        return map { it.copy(magnitude = it.magnitude * magnitudeMultiplier) }
    }

}
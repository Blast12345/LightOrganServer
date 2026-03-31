package lightOrgan.spectrum

import audio.samples.AudioFormat
import audio.samples.AudioFrame
import audio.samples.RollingAudioBuffer
import bins.FrequencyBins
import config.ConfigSingleton
import dsp.Downsampler
import dsp.MonoMixer
import dsp.ZeroPaddingInterpolator
import dsp.fft.FftFrequencyBinsCalculator
import dsp.windowing.HannWindow
import dsp.windowing.WindowFunction
import extensions.inSeconds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import math.nextPowerOfTwo

// ENHANCEMENT: Implement equal-loudness contours (ISO 226:2003). Manual SPL number with future plans of external meter?
// ENHANCEMENT: If implementing other calculation strategies (e.g., DFT, CZT), then create a bin calculator interface
// ENHANCEMENT: Explore sub-frame duration frequency calculation. Cool challenge, but probably not necessary for music.
// ENHANCEMENT: Inaccurate low frequencies — bins below the window duration are unreliable. Dual-FFT?
// ENHANCEMENT: Decimation - reduce the effective sample rate to increase performance.
// ENHANCEMENT: Improve handling of discontinuities (though I have doubt it is possible)
// ENHANCEMENT: Allow decimator frequency to be overridden; include use case like pre-filtered inputs and warn about aliasing if improperly configured
class SpectrumManager(
    private val config: SpectrumConfig = ConfigSingleton.spectrum,
    private val monoMixer: MonoMixer = MonoMixer(),
    private val filterManager: FilterManager = FilterManager(config.highPassFilter, config.lowPassFilter),
    private val downsampler: Downsampler = Downsampler(),
    private val audioBuffer: RollingAudioBuffer = RollingAudioBuffer(),
    private val windowFunction: WindowFunction = HannWindow(),
    private val interpolator: ZeroPaddingInterpolator = ZeroPaddingInterpolator(),
    private val frequencyBinsCalculator: FftFrequencyBinsCalculator = FftFrequencyBinsCalculator(),
) {

    private val _frequencyBins = MutableStateFlow<FrequencyBins>(emptyList())
    val frequencyBins: StateFlow<FrequencyBins> = _frequencyBins.asStateFlow()

    fun calculate(audio: AudioFrame): FrequencyBins {
        val conditionedAudio = conditionAudio(audio)
        val preparedFrame = prepareFrame(conditionedAudio)
        val allBins = frequencyBinsCalculator.calculate(preparedFrame, windowFunction.magnitudeCorrectionFactor)
        val relevantBins = filterBins(allBins, preparedFrame.format)

        _frequencyBins.value = relevantBins
        return relevantBins
    }

    // Conditioning
    private fun conditionAudio(audio: AudioFrame): AudioFrame {
        val higherStopbandFrequency = filterManager.lowPassThresholdFrequency(config.rolloffThreshold)
        val targetNyquist = higherStopbandFrequency ?: audio.format.nyquistFrequency

        return audio
            .let { monoMixer.mix(it) }
            .let { filterManager.filter(it) }
            .let { decimateIfNeeded(it, targetNyquist) }
    }

    private fun decimateIfNeeded(audio: AudioFrame, targetNyquist: Float): AudioFrame {
        val factor = downsampler.decimationFactor(audio.format.sampleRate, targetNyquist)
        val effectiveSampleRate = audio.format.sampleRate / factor

        if (factor <= 1) return audio

        return AudioFrame(
            samples = downsampler.decimate(audio.samples, factor, audio.format.sampleRate, audio.format.channels),
            format = audio.format.copy(sampleRate = effectiveSampleRate)
        )
    }

    // Frame Prep
    private fun prepareFrame(audio: AudioFrame): AudioFrame {
        val sampleSize = (config.frameDuration.inSeconds * audio.format.sampleRate).toInt()
        val samplesSizeForDesiredSpacing = audio.format.sampleRate / config.approximateBinSpacing
        val optimalFftLength = nextPowerOfTwo(samplesSizeForDesiredSpacing.toInt())

        return audio
            .let { audioBuffer.append(audio, sampleSize) }
            .let { applyWindowFunction(it) }
            .let { interpolate(it, optimalFftLength) }
    }

    private fun applyWindowFunction(audio: AudioFrame): AudioFrame {
        return AudioFrame(
            samples = windowFunction.appliedTo(audio.samples),
            format = audio.format
        )
    }

    private fun interpolate(audio: AudioFrame, targetSize: Int): AudioFrame {
        return AudioFrame(
            samples = interpolator.interpolate(audio.samples, targetSize),
            format = audio.format
        )
    }

    // Filtering
    private fun filterBins(bins: FrequencyBins, format: AudioFormat): FrequencyBins {
        val frequencyResolution = 1 / config.frameDuration.inSeconds
        val nyquist = format.nyquistFrequency
        val lowerStopbandFrequency = filterManager.highPassThresholdFrequency(config.rolloffThreshold)
        val higherStopbandFrequency = filterManager.lowPassThresholdFrequency(config.rolloffThreshold)

        val lowestFrequency = maxOf(frequencyResolution, lowerStopbandFrequency ?: frequencyResolution)
        val highestFrequency = minOf(nyquist, higherStopbandFrequency ?: nyquist)

        return bins.filter { it.frequency in lowestFrequency..highestFrequency }
    }

}
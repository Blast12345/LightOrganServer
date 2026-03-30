package lightOrgan.spectrum

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
    private val filterManager: FilterManager = FilterManager(),
    private val downsampler: Downsampler = Downsampler(),
    private val audioBuffer: RollingAudioBuffer = RollingAudioBuffer(),
    private val windowFunction: WindowFunction = HannWindow(),
    private val interpolator: ZeroPaddingInterpolator = ZeroPaddingInterpolator(),
    private val frequencyBinsCalculator: FftFrequencyBinsCalculator = FftFrequencyBinsCalculator(),
) {

    private val thresholdDb: Float = -48f

    private val _frequencyBins = MutableStateFlow<FrequencyBins>(emptyList())
    val frequencyBins: StateFlow<FrequencyBins> = _frequencyBins.asStateFlow()

    fun calculate(audio: AudioFrame): FrequencyBins {
        val highPassThreshold = filterManager.highPassThresholdFrequency(thresholdDb, audio.format.sampleRate)
        val lowPassThreshold = filterManager.lowPassThresholdFrequency(thresholdDb, audio.format.sampleRate)

        // Signal processing
        var processedAudio = monoMixer.mix(audio)
        processedAudio = filterManager.filter(processedAudio)
        processedAudio = decimateIfNeeded(processedAudio, lowPassThreshold)

        // Frame preparation
        val sampleSize = (config.frameDuration.inSeconds * processedAudio.format.sampleRate).toInt()
        val fftSize = nextPowerOfTwo((processedAudio.format.sampleRate / config.approximateBinSpacing).toInt())

        var preparedFrame = audioBuffer.append(processedAudio, sampleSize)
        preparedFrame = windowFunction.appliedTo(preparedFrame)
        preparedFrame = interpolator.interpolate(preparedFrame, fftSize)

        // Bin generation
        val allBins = frequencyBinsCalculator.calculate(preparedFrame, windowFunction.magnitudeCorrectionFactor)

        // Filtering
        val frequencyResolution = 1 / config.frameDuration.inSeconds
        val nyquist = preparedFrame.format.nyquistFrequency
        val lowestFrequency = maxOf(frequencyResolution, highPassThreshold ?: frequencyResolution)
        val highestFrequency = minOf(nyquist, lowPassThreshold ?: nyquist)

        val relevantBins = allBins
            .filter { it.frequency >= lowestFrequency }
            .filter { it.frequency <= highestFrequency }

        // Result
        _frequencyBins.value = relevantBins
        return relevantBins
    }

    private fun decimateIfNeeded(audio: AudioFrame, thresholdFrequency: Float?): AudioFrame {
        if (thresholdFrequency == null) return audio
        val factor = downsampler.decimationFactor(audio.format.sampleRate, thresholdFrequency)
        if (factor <= 1) return audio

        val samples = downsampler.decimate(audio.samples, factor, audio.format.sampleRate, audio.format.channels)
        return AudioFrame(samples, audio.format.copy(sampleRate = audio.format.sampleRate / factor))
    }

    // TODO: Probably a global function?
    private fun nextPowerOfTwo(value: Int): Int {
        require(value > 0) { "Value must be positive, got $value" }
        if (value and (value - 1) == 0) return value
        return Integer.highestOneBit(value) shl 1
    }

    private fun WindowFunction.appliedTo(audio: AudioFrame): AudioFrame {
        return AudioFrame(
            samples = appliedTo(audio.samples),
            format = audio.format
        )
    }

    private fun ZeroPaddingInterpolator.interpolate(audio: AudioFrame, targetSize: Int): AudioFrame {
        return AudioFrame(
            samples = interpolate(audio.samples, targetSize),
            format = audio.format
        )
    }

}
package lightOrgan.spectrum

import audio.samples.AudioFrame
import audio.samples.RollingAudioBuffer
import bins.FrequencyBins
import config.ConfigSingleton
import dsp.MonoMixer
import dsp.ZeroPaddingInterpolator
import dsp.fft.FftFrequencyBinsCalculator
import dsp.windowing.HannWindow
import dsp.windowing.WindowFunction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ENHANCEMENT: Implement equal-loudness contours (ISO 226:2003). Manual SPL number with future plans of external meter?
// ENHANCEMENT: If implementing other calculation strategies (e.g., DFT, CZT), then create a bin calculator interface
// ENHANCEMENT: Explore sub-frame duration frequency calculation. Cool challenge, but probably not necessary for music.
// ENHANCEMENT: Inaccurate low frequencies — bins below the window duration are unreliable. Dual-FFT?
// ENHANCEMENT: Decimation - reduce the effective sample rate to increase performance.
// ENHANCEMENT: Improve handling of discontinuities (though I have doubt it is possible)
class SpectrumManager(
    private val config: SpectrumConfig = ConfigSingleton.spectrum,
    private val monoMixer: MonoMixer = MonoMixer(),
    private val filterManager: FilterManager = FilterManager(config.highPassFilter, config.lowPassFilter),
    private val downsampler: Downsampler = Downsampler(),
    private val audioBuffer: RollingAudioBuffer = RollingAudioBuffer(config.sampleSize),
    private val windowFunction: WindowFunction = HannWindow(),
    private val interpolator: ZeroPaddingInterpolator = ZeroPaddingInterpolator(),
    private val frequencyBinsCalculator: FftFrequencyBinsCalculator = FftFrequencyBinsCalculator(),
) {

    val thresholdDb = -48f

    private val _frequencyBins = MutableStateFlow<FrequencyBins>(emptyList())
    val frequencyBins: StateFlow<FrequencyBins> = _frequencyBins.asStateFlow()

    fun calculate(audio: AudioFrame): FrequencyBins {
        // Signal processing
        var processedAudio = monoMixer.mix(audio) // TODO: x.interleave
//        processedAudio = filterManager.filter(processedAudio)

        val highestFrequency = filterManager.highestPassingFrequency(audio.format.sampleRate, thresholdDb)

        if (highestFrequency != null) {
//            processedAudio = downsampler.decimate(processedAudio, highestFrequency)
        }

        // Frame preparation
        var preparedFrame = audioBuffer.append(processedAudio) // TODO: Expose a duration?
        preparedFrame = windowFunction.appliedTo(preparedFrame)
        preparedFrame = interpolator.interpolate(preparedFrame)

        // Bin generation
        val allBins = frequencyBinsCalculator.calculate(preparedFrame, windowFunction.magnitudeCorrectionFactor)

        val frameDuration = config.sampleSize / audio.format.sampleRate
        val minimumCalculableFrequency = 1 / frameDuration

        val relevantBins = allBins
//            .filter { it.frequency > minimumCalculableFrequency }
            .filter { it.frequency < 218f }

        // Result
        _frequencyBins.value = relevantBins
        return relevantBins
    }

    private fun WindowFunction.appliedTo(audio: AudioFrame): AudioFrame {
        return AudioFrame(appliedTo(audio.samples), audio.format)
    }

    private fun ZeroPaddingInterpolator.interpolate(audio: AudioFrame): AudioFrame {
        return AudioFrame(interpolate(audio.samples, config.interpolatedSampleSize), audio.format)
    }

}
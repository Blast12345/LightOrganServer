package lightOrgan.spectrum

import audio.samples.AudioFrame
import bins.FrequencyBins
import config.ConfigSingleton
import dsp.MonoMixer
import dsp.SampleFramer
import dsp.ZeroPaddingInterpolator
import dsp.fft.FftFrequencyBinsCalculator
import dsp.windowing.HannWindow
import dsp.windowing.WindowFunction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ENHANCEMENT: Implement equal-loudness contours (ISO 226:2003). Manual SPL number with future plans of external meter?
// ENHANCEMENT: If implementing other calculation strategies (e.g., DFT, CZT), then create a bin calculator interface
// ENHANCEMENT: Make scaling configurable
// ENHANCEMENT: Explore sub-frame duration frequency calculation. Cool challenge, but probably not necessary for music.
class SpectrumManager(
    private val monoMixer: MonoMixer = MonoMixer(),
    private val sampleFramer: SampleFramer = SampleFramer(),
    private val windowFunction: WindowFunction = HannWindow(),
    private val interpolator: ZeroPaddingInterpolator = ZeroPaddingInterpolator(),
    private val fftFrequencyBinsCalculator: FftFrequencyBinsCalculator = FftFrequencyBinsCalculator(),
    private val magnitudeMultiplier: Float = ConfigSingleton.magnitudeMultiplier
) {

    private val _frequencyBins = MutableStateFlow<FrequencyBins>(emptyList())
    val frequencyBins: StateFlow<FrequencyBins> = _frequencyBins.asStateFlow()

    fun calculate(audio: AudioFrame): FrequencyBins {
        // Signal Processing
        val monoAudio = monoMixer.mix(audio)
        val frame = sampleFramer.frame(monoAudio.samples, 0)
        val windowedFrame = windowFunction.appliedTo(frame)
        val interpolatedFrame = interpolator.interpolate(windowedFrame)

        // Bin generation
        val frameDuration = sampleFramer.frameSize / audio.format.sampleRate
        val minimumCalculableFrequency = 1 / frameDuration
        val minimumUsefulFrequency = minimumCalculableFrequency// * 1.5 // TODO: Subjective

        val allBins = fftFrequencyBinsCalculator
            .calculate(interpolatedFrame, monoAudio.format)
            .applyWindowCorrection()
            .applyMagnitudeMultiplier()
            // TODO: Test
            .filter { it.frequency > minimumUsefulFrequency } // This class a has a responsibility to filter out bad data

        _frequencyBins.value = allBins

        return allBins
    }

    private fun FrequencyBins.applyWindowCorrection(): FrequencyBins {
        return map { it.copy(magnitude = it.magnitude * windowFunction.amplitudeCorrectionFactor) }
    }

    private fun FrequencyBins.applyMagnitudeMultiplier(): FrequencyBins {
        return map { it.copy(magnitude = it.magnitude * magnitudeMultiplier) }
    }

}
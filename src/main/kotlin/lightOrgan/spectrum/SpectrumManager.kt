package lightOrgan.spectrum

import audio.samples.AudioFrame
import dsp.MonoMixer
import dsp.SampleFramer
import dsp.ZeroPaddingInterpolator
import dsp.fft.FrequencyBins
import dsp.fft.FrequencyBinsCalculator
import dsp.windowing.HannWindow
import dsp.windowing.WindowFunction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import sound.bins.frequency.FrequencyBinsFilter

// ENHANCEMENT: Allow filter to be configured; expose FrequencyBinsFilter.
// ENHANCEMENT: If implementing other calculation strategies (e.g. DFT, CZT), then create a bin calculator interface
class SpectrumManager(
    private val monoMixer: MonoMixer = MonoMixer(),
    private val sampleFramer: SampleFramer = SampleFramer(),
    private val windowFunction: WindowFunction = HannWindow(),
    private val interpolator: ZeroPaddingInterpolator = ZeroPaddingInterpolator(),
    private val frequencyBinsCalculator: FrequencyBinsCalculator = FrequencyBinsCalculator(),
    private val frequencyBinsFilter: FrequencyBinsFilter = FrequencyBinsFilter()
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
        val allBins = frequencyBinsCalculator
            .calculate(interpolatedFrame, monoAudio.format)
            .applyAmplitudeCorrection(windowFunction.amplitudeCorrectionFactor)

        val filteredBins = frequencyBinsFilter.filter(allBins)

        _frequencyBins.value = filteredBins

        return filteredBins
    }

    private fun FrequencyBins.applyAmplitudeCorrection(factor: Float): FrequencyBins {
        return map { it.copy(magnitude = it.magnitude * factor) }
    }

}
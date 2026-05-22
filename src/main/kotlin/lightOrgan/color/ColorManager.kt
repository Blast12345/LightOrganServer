package lightOrgan.color

import color.SrgbColor
import color.SrgbColors
import dsp.bins.FrequencyBins
import dsp.peakExtraction.ParabolicSpectralPeakExtractor
import dsp.peakExtraction.SpectralPeakExtractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ENHANCEMENT: OKLCH (or other perceptually balanced spectrum)
// ENHANCEMENT: Color smoothing
// ENHANCEMENT: Force a given hue, saturation, or color.
class ColorManager(
    private val peakExtractor: SpectralPeakExtractor = ParabolicSpectralPeakExtractor(),
    private val colorAlgorithm: ColorAlgorithm = ColorWheelAlgorithm(),
) {

    private val _color = MutableStateFlow(SrgbColors.Black)
    val color: StateFlow<SrgbColor> = _color.asStateFlow()

    fun calculate(frequencyBins: FrequencyBins): SrgbColor {
        val peaks = peakExtractor.extract(frequencyBins)
        _color.value = colorAlgorithm.calculate(peaks)
        return _color.value
    }

}
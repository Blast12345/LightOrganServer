package lightOrgan.color

import color.RgbColor
import dsp.bins.FrequencyBins
import dsp.peakExtraction.ParabolicSpectralPeakExtractor
import dsp.peakExtraction.SpectralPeakExtractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ENHANCEMENT: Color smoothing
// ENHANCEMENT: Force a given hue, saturation, or color.
class ColorManager(
    private val peakExtractor: SpectralPeakExtractor = ParabolicSpectralPeakExtractor(),
    private val colorAlgorithm: ColorAlgorithm = ColorWheelAlgorithm(),
) {

    private val _color = MutableStateFlow(RgbColor.Black)
    val color: StateFlow<RgbColor> = _color.asStateFlow()

    fun calculate(frequencyBins: FrequencyBins): RgbColor {
        val peaks = peakExtractor.extract(frequencyBins)
        _color.value = colorAlgorithm.calculate(peaks)
        return _color.value
    }

}
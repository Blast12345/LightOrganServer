package lightOrgan.color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import dsp.bins.FrequencyBins
import dsp.peakExtraction.ParabolicSpectralPeakExtractor
import dsp.peakExtraction.SpectralPeakExtractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ENHANCEMENT: Color smoothing
// ENHANCEMENT: Perceptually uniform hue mapping — uniform octave position mapping doesn't produce equally distinguishable colors across the hue wheel. Mixing in a perceptually uniform color space like OKLAB/OKLCH and converting to RGB for output would make the mapping more consistent. The conversion should be exposed through the Color wrapper object (e.g. initialize with HSB, convert via something like `toColorSpace(oklab)`).
// ENHANCEMENT: Force a given hue, saturation, or color.
class ColorManager(
    private val peakExtractor: SpectralPeakExtractor = ParabolicSpectralPeakExtractor(),
    private val colorAlgorithm: ColorAlgorithm = ColorWheelAlgorithm(),
) {

    private val _color = MutableStateFlow(Color.Black)
    val color: StateFlow<Color> = _color.asStateFlow()

    fun calculate(frequencyBins: FrequencyBins): Color {
        val peaks = peakExtractor.extract(frequencyBins)

        val srgbColor = if (peaks.isEmpty()) SrgbColor.Black else colorAlgorithm.calculate(peaks)

        val composeColor = Color(
            srgbColor.red.value.toFloat(),
            srgbColor.green.value.toFloat(),
            srgbColor.blue.value.toFloat(),
            colorSpace = ColorSpaces.Srgb
        )

        _color.value = composeColor
        return composeColor
    }

}
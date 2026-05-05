package lightOrgan.color

import androidx.compose.ui.graphics.Color
import dsp.bins.FrequencyBins
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ENHANCEMENT: Perceptually uniform hue mapping — uniform octave position mapping doesn't produce equally distinguishable colors across the hue wheel. Mixing in a perceptually uniform color space like OKLAB/OKLCH and converting to RGB for output would make the mapping more consistent. The conversion should be exposed through the Color wrapper object (e.g. initialize with HSB, convert via something like `toColorSpace(oklab)`).
// ENHANCEMENT: Force a given hue, saturation, or color.
class ColorManager(
    private val peakFrequencyBinsFinder: PeakFrequencyBinsFinder = PeakFrequencyBinsFinder(),
    private val colorCalculator: ColorCalculator = ColorCalculator(),
) {

    private val _color = MutableStateFlow(Color.Black)
    val color: StateFlow<Color> = _color.asStateFlow()

    fun calculate(frequencyBins: FrequencyBins): Color {
        val peakFrequencyBins = peakFrequencyBinsFinder.find(frequencyBins)

        val color = if (peakFrequencyBins.isEmpty()) Color.Black else colorCalculator.calculate(peakFrequencyBins)

        _color.value = color
        return color
    }

}
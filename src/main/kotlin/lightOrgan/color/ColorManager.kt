package lightOrgan.color

import androidx.compose.ui.graphics.Color
import bins.FrequencyBins
import bins.PeakFrequencyBinsCalculator
import config.ConfigSingleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// TODO: Is this actually a LightManager? Do we return light objects and consumers are responsible for turning Light into concrete colors?
// But it wouldn't know how to handle brightness.

// ENHANCEMENT: Chaldi patterns
// ENHANCEMENT: Expose configurability
// ENHANCEMENT: Noise floor suppression — background noise (especially in live environments) dilutes the hue toward average values. Needs a context-sensitive approach (aggressive suppression at a festival, minimal at home with clean audio). Hard cutoffs and naive scaling both have problems. Solving this may also help with sidelobe contamination at low frequencies and rolled-off high bins pulling the hue.
// ENHANCEMENT: Perceptually uniform hue mapping — uniform octave position mapping doesn't produce equally distinguishable colors across the hue wheel. Mixing in a perceptually uniform color space like OKLAB/OKLCH and converting to RGB for output would make the mapping more consistent. The conversion should be exposed through the Color wrapper object (e.g. initialize with HSB, convert via something like `toColorSpace(oklab)`).
// ENHANCEMENT: Force a given hue, saturation, or color.
class ColorManager(
    private val brightnessMultiplier: Float = ConfigSingleton.brightnessMultiplier,
    private val peakFrequencyBinsCalculator: PeakFrequencyBinsCalculator = PeakFrequencyBinsCalculator(),
    private val colorCalculator: ColorCalculator = ColorCalculator(),
) {


    private val _colors = MutableStateFlow(listOf(Color.Black, Color.Black, Color.Black, Color.Black))
    val colors: StateFlow<List<Color>> = _colors.asStateFlow()


    fun calculate(frequencyBins: FrequencyBins): Color { // TODO: Return metadata?
        val peakBins = peakFrequencyBinsCalculator.calculate(frequencyBins)

        val colors = if (peakBins.isEmpty()) listOf(
            Color.Black,
            Color.Black,
            Color.Black,
            Color.Black
        ) else colorCalculator.calculate(peakBins, brightnessMultiplier)

        _colors.value = colors

        return colors.first()
    }

}
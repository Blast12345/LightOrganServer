package lightOrgan.color

import androidx.compose.ui.graphics.Color
import config.ConfigSingleton
import dsp.bins.FrequencyBin
import dsp.bins.FrequencyBins
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// TODO: Is this actually a LightManager? Do we return light objects and consumers are responsible for turning Light into concrete colors?
// But it wouldn't know how to handle brightness.

// ENHANCEMENT: Noise floor rejection
// ENHANCEMENT: Chaldi patterns
// ENHANCEMENT: Expose configurability
// ENHANCEMENT: Noise floor suppression — background noise (especially in live environments) dilutes the hue toward average values. Needs a context-sensitive approach (aggressive suppression at a festival, minimal at home with clean audio). Hard cutoffs and naive scaling both have problems. Solving this may also help with sidelobe contamination at low frequencies and rolled-off high bins pulling the hue.
// ENHANCEMENT: Perceptually uniform hue mapping — uniform octave position mapping doesn't produce equally distinguishable colors across the hue wheel. Mixing in a perceptually uniform color space like OKLAB/OKLCH and converting to RGB for output would make the mapping more consistent. The conversion should be exposed through the Color wrapper object (e.g. initialize with HSB, convert via something like `toColorSpace(oklab)`).
// ENHANCEMENT: Force a given hue, saturation, or color.
// ENHANCEMENT: Gamma correction on clients
class ColorManager(
    private val brightnessMultiplier: Float = ConfigSingleton.brightnessMultiplier,
    private val peakFrequencyBinsCalculator: PeakFrequencyBinsCalculator = PeakFrequencyBinsCalculator(),
    private val prominenceCalculator: ProminenceCalculator = ProminenceCalculator(),
    private val colorCalculator: ColorCalculator = ColorCalculator(),
) {

    private val _color = MutableStateFlow(Color.Black)
    val color: StateFlow<Color> = _color.asStateFlow()

    // TODO: Reject peaks that are from aliasing?
    // TODO: Reject peaks that are from sidelobes (or compensate)
    fun calculate(frequencyBins: FrequencyBins): Color { // TODO: Return metadata?
        val peakBins = peakFrequencyBinsCalculator.calculate(frequencyBins)
        val peaksWithProminence = prominenceCalculator.calculate(peakBins, frequencyBins)
        val prominentBins = peaksWithProminence.filter { it.prominence > 0.0005 }.map { it.frequencyBin }

        val color =
            if (peakBins.isEmpty()) Color.Black else colorCalculator.calculate(prominentBins, brightnessMultiplier)

        // Return
        _color.value = color
        return color
    }

}


data class PeakWithProminence(
    val frequencyBin: FrequencyBin,
    val prominence: Float
)

class ProminenceCalculator {

    fun calculate(peaks: FrequencyBins, spectrum: FrequencyBins): List<PeakWithProminence> {
        if (peaks.isEmpty()) return emptyList()

        val sortedPeaks = peaks.sortedBy { it.frequency }
        val sortedSpectrum = spectrum.sortedBy { it.frequency }

        return sortedPeaks.mapIndexed { index, peak ->
            val leftValley = findValley(
                spectrum = sortedSpectrum,
                from = sortedPeaks.getOrNull(index - 1)?.frequency ?: sortedSpectrum.first().frequency,
                to = peak.frequency
            )

            val rightValley = findValley(
                spectrum = sortedSpectrum,
                from = peak.frequency,
                to = sortedPeaks.getOrNull(index + 1)?.frequency ?: sortedSpectrum.last().frequency
            )

            val referenceLevel = maxOf(leftValley, rightValley)

            PeakWithProminence(
                frequencyBin = peak,
                prominence = peak.magnitude - referenceLevel
            )
        }
    }

    private fun findValley(spectrum: List<FrequencyBin>, from: Float, to: Float): Float {
        return spectrum
            .filter { it.frequency in from..to }
            .minOfOrNull { it.magnitude }
            ?: 0f
    }
}
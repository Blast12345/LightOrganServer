package lightOrgan.color.calculator

import bins.FrequencyBins
import lightOrgan.color.calculator.hue.HueCalculator
import lightOrgan.color.calculator.hue.OctaveHueCalculator
import wrappers.color.Color

// ENHANCEMENT: Saturation calculator — factor in harmonics or variance over time
// ENHANCEMENT: Inaccurate low frequencies — bins below the window duration are unreliable. Dual-FFT / temporal smoothing approach is being considered at the spectrum layer.
// ENHANCEMENT: Tonality vs. transients — favor sustained tonal content over transients like kicks for hue, without sacrificing responsiveness to rapid tonal changes (e.g. bass wubs). Saturation could serve as a confidence signal for how tonal the current content is.
// ENHANCEMENT: Noise floor suppression — background noise (especially in live environments) dilutes the hue toward average values. Needs a context-sensitive approach (aggressive suppression at a festival, minimal at home with clean audio). Hard cutoffs and naive scaling both have problems. Solving this may also help with sidelobe contamination at low frequencies and rolled-off high bins pulling the hue.
// ENHANCEMENT: Shared vs. independent data preparation — hue and brightness calculators should agree on what they're looking at but not on how they interpret it. Both should be independently swappable strategies that still produce coherent colors. The open question is what the right shared input layer is — raw bins are too low, fully prepared peaks are too high, and the right middle layer depends on which strategies actually get built.
// ENHANCEMENT: Perceptually uniform hue mapping — uniform octave position mapping doesn't produce equally distinguishable colors across the hue wheel. Mixing in a perceptually uniform color space like OKLAB/OKLCH and converting to RGB for output would make the mapping more consistent. The conversion should be exposed through the Color wrapper object (e.g. initialize with HSB, convert via something like `toColorSpace(oklab)`).
class ColorCalculator(
    private val hueCalculator: HueCalculator = OctaveHueCalculator(),
    private val brightnessCalculator: BrightnessCalculator = BrightnessCalculator()
) {

    fun calculate(frequencyBins: FrequencyBins): Color? {
        val hue = hueCalculator.calculate(frequencyBins) ?: return null
        val brightness = brightnessCalculator.calculate(frequencyBins) ?: return null

        return Color(hue, 1F, brightness)
    }

}
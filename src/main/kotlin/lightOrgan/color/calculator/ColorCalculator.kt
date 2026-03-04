package lightOrgan.color.calculator

import bins.frequency.FrequencyBins
import lightOrgan.color.calculator.hue.HueCalculator
import lightOrgan.color.calculator.hue.OctaveHueCalculator
import wrappers.color.Color

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
package lightOrgan.color

import color.BrightnessCalculator
import color.HueCalculator
import dsp.fft.FrequencyBins
import wrappers.color.Color

class ColorCalculator(
    private val hueCalculator: HueCalculator = HueCalculator(),
    private val brightnessCalculator: BrightnessCalculator = BrightnessCalculator()
) {

    fun calculate(frequencyBins: FrequencyBins): Color? {
        val hue = hueCalculator.calculate(frequencyBins) ?: return null
        val brightness = brightnessCalculator.calculate(frequencyBins) ?: return null

        return Color(hue, 1F, brightness)
    }

}
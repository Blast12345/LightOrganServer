package color

import androidx.compose.ui.graphics.Color
import dsp.bins.FrequencyBins

// TODO: Rename to ColorCalculator
class ColorFactory(
    private val hueCalculator: HueCalculator = HueCalculator(),
    private val brightnessCalculator: BrightnessCalculator = BrightnessCalculator()
) {

    @Suppress("ReturnCount")
    fun create(frequencyBins: FrequencyBins): Color {
        val hue = getHue(frequencyBins) ?: return Color.Black
        val brightness = getBrightness(frequencyBins) ?: return Color.Black

        return Color.hsv(hue * 360f, 1F, brightness)
    }

    private fun getHue(bassBins: FrequencyBins): Float? {
        return hueCalculator.calculate(bassBins)
    }

    private fun getBrightness(bassBins: FrequencyBins): Float? {
        return brightnessCalculator.calculate(bassBins)
    }

}

package color

import sound.bins.frequencyBins.FrequencyBin
import wrappers.color.Color

class ColorFactory(
    private val hueCalculator: HueCalculator = HueCalculator(),
    private val brightnessFactory: BrightnessFactory = BrightnessFactory()
) {

    private val black = Color(0F, 0F, 0F)

    fun create(frequencyBin: FrequencyBin?): Color {
        if (frequencyBin == null) {
            return black
        }

        return Color(
            hue = getHue(frequencyBin),
            saturation = 1F,
            brightness = getBrightness(frequencyBin)
        )
    }

    private fun getHue(frequencyBin: FrequencyBin): Float {
        return hueCalculator.calculate(frequencyBin.frequency)
    }

    private fun getBrightness(frequencyBin: FrequencyBin): Float {
        return brightnessFactory.create(frequencyBin.magnitude)
    }

}


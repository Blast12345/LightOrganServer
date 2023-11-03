package color

import sound.frequencyBins.FrequencyBin
import wrappers.color.Color

class ColorFactory(
    private val hueFactory: HueFactory = HueFactory(),
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
        return hueFactory.create(frequencyBin.frequency)
    }

    private fun getBrightness(frequencyBin: FrequencyBin): Float {
        return brightnessFactory.create(frequencyBin.magnitude)
    }

}


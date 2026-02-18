package color

import org.greenrobot.eventbus.EventBus
import sound.bins.frequency.FrequencyBins
import wrappers.color.Color

class ColorFactory(
    private val hueCalculator: HueCalculator = HueCalculator(),
    private val brightnessCalculator: BrightnessCalculator = BrightnessCalculator()
) {

    @Suppress("ReturnCount")
    fun create(frequencyBins: FrequencyBins): Color {
        EventBus.getDefault().post(frequencyBins)

        val hue = getHue(frequencyBins) ?: return Color.black
        val brightness = getBrightness(frequencyBins) ?: return Color.black

        return Color(hue, 1F, brightness)
    }

    private fun getHue(bassBins: FrequencyBins): Float? {
        return hueCalculator.calculate(bassBins)
    }

    private fun getBrightness(bassBins: FrequencyBins): Float? {
        return brightnessCalculator.calculate(bassBins)
    }

}

package color

import input.audioFrame.AudioFrame
import org.greenrobot.eventbus.EventBus
import sound.bins.frequency.BassBinsFactory
import sound.bins.frequency.FrequencyBins
import wrappers.color.Color

class ColorFactory(
    private val bassBinsFactory: BassBinsFactory = BassBinsFactory(),
    private val hueCalculator: HueCalculator = HueCalculator(),
    private val brightnessCalculator: BrightnessCalculator = BrightnessCalculator()
) {

    @Suppress("ReturnCount")
    fun create(audioFrame: AudioFrame): Color {
        val bassBins = bassBinsFactory.create(audioFrame)
        EventBus.getDefault().post(bassBins)

        val hue = getHue(bassBins) ?: return Color.black
        val brightness = getBrightness(bassBins) ?: return Color.black

        return Color(hue, 1F, brightness)
    }

    private fun getHue(bassBins: FrequencyBins): Float? {
        return hueCalculator.calculate(bassBins)
    }

    private fun getBrightness(bassBins: FrequencyBins): Float? {
        return brightnessCalculator.calculate(bassBins)
    }

}

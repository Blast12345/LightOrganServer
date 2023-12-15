package color

import input.audioFrame.AudioFrame
import sound.frequencyBins.BassBinsFactory
import sound.frequencyBins.FrequencyBinList
import wrappers.color.Color

class ColorFactory(
    private val bassBinsFactory: BassBinsFactory = BassBinsFactory(),
    private val hueCalculator: HueCalculator = HueCalculator(),
    private val brightnessCalculator: BrightnessCalculator = BrightnessCalculator()
) {

    fun create(audioFrame: AudioFrame): Color {
        val bassBins = bassBinsFactory.create(audioFrame)

        val hue = getHue(bassBins) ?: return Color.black
        val brightness = getBrightness(bassBins) ?: return Color.black

        return Color(hue, 1F, brightness)
    }

    private fun getHue(bassBins: FrequencyBinList): Float? {
        return hueCalculator.calculate(bassBins)
    }

    private fun getBrightness(bassBins: FrequencyBinList): Float? {
        return brightnessCalculator.calculate(bassBins)
    }

}
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
        return create(
            bassBins = bassBinsFactory.create(audioFrame)
        )
    }

    private fun create(bassBins: FrequencyBinList): Color {
        return Color(
            hue = getHue(bassBins) ?: return Color.black,
            saturation = 1F,
            brightness = getBrightness(bassBins) ?: return Color.black
        )
    }

    private fun getHue(bassBins: FrequencyBinList): Float? {
        return hueCalculator.calculate(bassBins)
    }

    private fun getBrightness(bassBins: FrequencyBinList): Float? {
        return brightnessCalculator.calculate(bassBins)
    }

}
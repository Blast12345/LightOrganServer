package color

import input.audioFrame.AudioFrame
import sound.bins.frequency.BassBinsFactory
import wrappers.color.Color

class ColorFactory(
    private val bassBinsFactory: BassBinsFactory = BassBinsFactory(),
    private val hueCalculator: HueCalculator = HueCalculator(),
    private val brightnessCalculator: BrightnessCalculator = BrightnessCalculator()
) {

    @Suppress("ReturnCount")
    fun create(audioFrame: AudioFrame): Color {
//        val bassBins = bassBinsFactory.create(audioFrame)

        val hue = hueCalculator.calculate(audioFrame) ?: return Color.black
        val brightness = brightnessCalculator.calculate(audioFrame) ?: return Color.black

        return Color(hue, 1F, brightness)
    }

}

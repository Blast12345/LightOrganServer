package color

import input.audioFrame.AudioFrame
import wrappers.color.Color

class ColorFactory(
    private val hueCalculator: HueCalculator = HueCalculator(),
    private val brightnessCalculator: BrightnessCalculator = BrightnessCalculator()
) {

    @Suppress("ReturnCount")
    fun create(audioFrame: AudioFrame): Color {
        val hue = hueCalculator.calculate(audioFrame) ?: return Color.black
        val brightness = brightnessCalculator.calculate(audioFrame) ?: return Color.black

        return Color(hue, 1F, brightness)
    }

}

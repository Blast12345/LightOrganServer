package colorService.color

import colorService.sound.FrequencyBin
import colorService.sound.FrequencyBins
import java.awt.Color

interface ColorGeneratorInterface {
    fun colorForFrequency(frequencyBins: List<FrequencyBin>): Color
}

class ColorGenerator: ColorGeneratorInterface {

    override fun colorForFrequency(frequencyBins: List<FrequencyBin>): Color {
        if (frequencyBins.isEmpty()) {
            return Color.black
        }

        // TODO: Maybe as TOTAL amplitude of the whole range increases, trend towards saturation 0 (aka - white)
        // TODO: As total amplitude approaches noise floor, lower brightness?
        return Color.getHSBColor(
            createHueForFrequencyBins(frequencyBins),
            1.0F,
            1.0F)
    }

    private fun createHueForFrequencyBins(frequencyBins: List<FrequencyBin>): Float {
        val binsModel = FrequencyBins(frequencyBins)
        val hue = (binsModel.averageFrequency() - binsModel.minimumFrequency()) / binsModel.maximumFrequency()
        return hue.toFloat()
    }

}
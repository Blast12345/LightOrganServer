package colorListener.color

import colorListener.sound.frequencyBins.FrequencyBin
import java.awt.Color

interface ColorFactoryInterface {
    fun colorFrom(frequencyBins: List<FrequencyBin>): Color
}

class ColorFactory(private val hueFactory: HueFactoryInterface = HueFactory()) : ColorFactoryInterface {

    override fun colorFrom(frequencyBins: List<FrequencyBin>): Color {
        if (frequencyBins.isEmpty()) {
            return Color.black
        }

        return Color.getHSBColor(
            hueFactory.hueFrom(frequencyBins),
            1.0F,
            1.0F
        )
    }

}
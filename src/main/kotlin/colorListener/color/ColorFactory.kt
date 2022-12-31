package colorListener.color

import sound.frequencyBins.FrequencyBin
import java.awt.Color

interface ColorFactoryInterface {
    fun colorFrom(frequencyBins: List<FrequencyBin>): Color
}

class ColorFactory(private val hueFactory: HueFactoryInterface = HueFactory()) : ColorFactoryInterface {

    override fun colorFrom(frequencyBins: List<FrequencyBin>): Color {
        val hue = hueFactory.hueFrom(frequencyBins)

        return if (hue != null) {
            Color.getHSBColor(hue, 1.0F, 1.0F)
        } else {
            Color.black
        }
    }

}
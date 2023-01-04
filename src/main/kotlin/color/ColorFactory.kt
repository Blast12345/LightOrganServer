package color

import sound.frequencyBins.FrequencyBins
import java.awt.Color

interface ColorFactoryInterface {
    fun create(frequencyBins: FrequencyBins): Color
}

class ColorFactory(
    private val hueFactory: HueFactoryInterface = HueFactory()
) : ColorFactoryInterface {

    override fun create(frequencyBins: FrequencyBins): Color {
        val hue = getHue(frequencyBins)

        return if (hue != null) {
            Color.getHSBColor(hue, 1.0F, 1.0F)
        } else {
            Color.black
        }
    }

    private fun getHue(frequencyBins: FrequencyBins): Float? {
        return hueFactory.create(frequencyBins)
    }

}
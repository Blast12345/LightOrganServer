package color

import sound.frequencyBins.FrequencyBins
import java.awt.Color

interface ColorFactoryInterface {
    fun createFor(frequencyBins: FrequencyBins): Color
}

class ColorFactory(
    private val hueFactory: HueFactoryInterface = HueFactory()
) : ColorFactoryInterface {

    override fun createFor(frequencyBins: FrequencyBins): Color {
        val hue = getHueFor(frequencyBins)

        return if (hue != null) {
            Color.getHSBColor(hue, 1.0F, 1.0F)
        } else {
            Color.black
        }
    }

    private fun getHueFor(frequencyBins: FrequencyBins): Float? {
        return hueFactory.createFrom(frequencyBins)
    }

}
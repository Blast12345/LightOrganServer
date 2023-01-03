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
        // TODO: Limit scope
        val subBins = frequencyBins.filter { it.frequency in 20.0..120.0 }.filter { it.amplitude > 0.01 }
        return hueFactory.create(subBins)
    }

}
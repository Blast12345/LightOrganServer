package color

import sound.frequencyBins.FrequencyBins
import sound.frequencyBins.FrequencyBinsFactory
import sound.frequencyBins.FrequencyBinsFactoryInterface
import sound.input.samples.AudioSignal
import java.awt.Color

interface ColorFactoryInterface {
    fun create(audioSignal: AudioSignal): Color
}

class ColorFactory(
    private val frequencyBinsFactory: FrequencyBinsFactoryInterface = FrequencyBinsFactory(),
    private val hueFactory: HueFactoryInterface = HueFactory()
) : ColorFactoryInterface {

    override fun create(audioSignal: AudioSignal): Color {
        val frequencyBins = getFrequencyBins(audioSignal)
        return getColor(frequencyBins)
    }

    private fun getFrequencyBins(audioSignal: AudioSignal): FrequencyBins {
        return frequencyBinsFactory.create(audioSignal)
    }

    private fun getColor(frequencyBins: FrequencyBins): Color {
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
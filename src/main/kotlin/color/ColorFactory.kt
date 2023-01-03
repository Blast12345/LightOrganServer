package color

import sound.frequencyBins.FrequencyBins
import java.awt.Color

interface ColorFactoryInterface {
    //    fun createFor(audioFrame: NormalizedAudioFrame): Color
    fun createFor(frequencyBins: FrequencyBins): Color
}

class ColorFactory(
    private val hueFactory: HueFactoryInterface = HueFactory()
) : ColorFactoryInterface {

    // NOTE: The minimum frequency dictates the length of audio needed to generate frequency bins.
    // The length of audio needed is: 1 / frequency = seconds
    // Example: 20hz requires 0.05 seconds of audio (1 / 20 = 0.05)
    // Longer audio increases latency because we make calculations based on increasingly older data
    private val lowestSupportedFrequency = 20F

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
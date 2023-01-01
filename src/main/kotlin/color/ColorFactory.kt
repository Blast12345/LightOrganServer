package color

import sound.frequencyBins.FrequencyBins
import sound.frequencyBins.FrequencyBinsFactory
import sound.frequencyBins.FrequencyBinsFactoryInterface
import sound.input.sample.AudioFrame
import java.awt.Color

interface ColorFactoryInterface {
    fun colorFor(audioFrame: AudioFrame): Color
}

class ColorFactory(
    private val frequencyBinsFactory: FrequencyBinsFactoryInterface = FrequencyBinsFactory(),
    private val hueFactory: HueFactoryInterface = HueFactory()
) : ColorFactoryInterface {

    // NOTE: The minimum frequency dictates the length of audio needed to generate frequency bins.
    // The length of audio needed is: 1 / frequency = seconds
    // Example: 20hz requires 0.05 seconds of audio (1 / 20 = 0.05)
    // Longer audio increases latency because we make calculations based on increasingly older data
    private val lowestSupportedFrequency = 20F

    override fun colorFor(audioFrame: AudioFrame): Color {
        val hue = getHueFor(audioFrame)

        return if (hue != null) {
            Color.getHSBColor(hue, 1.0F, 1.0F)
        } else {
            Color.black
        }
    }

    private fun getHueFor(audioFrame: AudioFrame): Float? {
        val frequencyBins = getFrequencyBinsFor(audioFrame)
        return hueFactory.createFrom(frequencyBins)
    }

    private fun getFrequencyBinsFor(audioFrame: AudioFrame): FrequencyBins {
        return frequencyBinsFactory.createFrom(audioFrame, lowestSupportedFrequency)
    }

}
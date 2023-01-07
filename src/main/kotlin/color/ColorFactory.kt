package color

import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.FrequencyBinsService
import sound.frequencyBins.FrequencyBinsServiceInterface
import sound.input.samples.AudioSignal
import java.awt.Color

interface ColorFactoryInterface {
    fun create(audioSignal: AudioSignal): Color
}

class ColorFactory(
    private val frequencyBinsService: FrequencyBinsServiceInterface = FrequencyBinsService(),
    private val hueFactory: HueFactoryInterface = HueFactory()
) : ColorFactoryInterface {

    override fun create(audioSignal: AudioSignal): Color {
        val frequencyBins = getFrequencyBins(audioSignal)
        return getColor(frequencyBins)
    }

    private fun getFrequencyBins(audioSignal: AudioSignal): FrequencyBinList {
        return frequencyBinsService.getFrequencyBins(audioSignal)
    }

    private fun getColor(frequencyBins: FrequencyBinList): Color {
        val hue = getHue(frequencyBins)

        return if (hue != null) {
            Color.getHSBColor(hue, 1.0F, 1.0F)
        } else {
            Color.black
        }
    }

    private fun getHue(frequencyBins: FrequencyBinList): Float? {
        return hueFactory.create(frequencyBins)
    }

}
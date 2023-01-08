package color

import color.dominantFrequency.DominantFrequencyBinFinder
import color.dominantFrequency.DominantFrequencyBinFinderInterface
import sound.frequencyBins.FrequencyBin
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
    private val dominantFrequencyBinFinder: DominantFrequencyBinFinderInterface = DominantFrequencyBinFinder(),
    private val hueFactory: HueFactoryInterface = HueFactory(),
    private val brightnessFactory: BrightnessFactoryInterface = BrightnessFactory()
) : ColorFactoryInterface {

    override fun create(audioSignal: AudioSignal): Color {
        val frequencyBins = getFrequencyBins(audioSignal)
        val dominantBin = getDominantFrequencyBin(frequencyBins)
        return getColor(dominantBin, frequencyBins)
    }

    private fun getFrequencyBins(audioSignal: AudioSignal): FrequencyBinList {
        return frequencyBinsService.getFrequencyBins(audioSignal)
    }

    private fun getDominantFrequencyBin(frequencyBins: FrequencyBinList): FrequencyBin {
        return dominantFrequencyBinFinder.find(frequencyBins)
    }

    private fun getColor(dominantBin: FrequencyBin, frequencyBins: FrequencyBinList): Color {
        return getColor(
            hue = getHue(dominantBin, frequencyBins),
            saturation = getSaturation(),
            brightness = getBrightness(dominantBin)
        )
    }

    private fun getHue(dominantBin: FrequencyBin, frequencyBinList: FrequencyBinList): Float? {
        return hueFactory.create(dominantBin, frequencyBinList)
    }

    private fun getSaturation(): Float {
        return 1F
    }

    private fun getBrightness(dominantBin: FrequencyBin): Float {
        return brightnessFactory.create(dominantBin)
    }

    private fun getColor(hue: Float?, saturation: Float, brightness: Float): Color {
        return if (hue != null) {
            Color.getHSBColor(hue, saturation, brightness)
        } else {
            Color.black
        }
    }

}
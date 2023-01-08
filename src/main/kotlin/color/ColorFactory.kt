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
        return Color.getHSBColor(
            getHue(dominantBin, frequencyBins),
            getSaturation(),
            getBrightness(dominantBin)
        )
    }

    private fun getHue(dominantBin: FrequencyBin, frequencyBinList: FrequencyBinList): Float {
        // TODO: Update hue factory to return non-optional
        return hueFactory.create(frequencyBinList)!!
    }

    private fun getSaturation(): Float {
        return 1F
    }

    private fun getBrightness(dominantBin: FrequencyBin): Float {
        return brightnessFactory.create(dominantBin)
    }

}
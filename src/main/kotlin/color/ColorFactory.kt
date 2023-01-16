package color

import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.FrequencyBinsService
import sound.frequencyBins.FrequencyBinsServiceInterface
import sound.frequencyBins.dominantFrequency.DominantFrequencyBinFactory
import sound.frequencyBins.dominantFrequency.DominantFrequencyBinFactoryInterface
import sound.frequencyBins.filters.BassBinsFilter
import sound.frequencyBins.filters.BassBinsFilterInterface
import sound.input.samples.AudioSignal
import java.awt.Color

interface ColorFactoryInterface {
    fun create(audioSignal: AudioSignal): Color
}

class ColorFactory(
    // TODO: Maybe there should be a BassBinsService? Creating AND filtering the bins here feels smelly.
    private val frequencyBinsService: FrequencyBinsServiceInterface = FrequencyBinsService(),
    private val bassBinsFilter: BassBinsFilterInterface = BassBinsFilter(),
    private val dominantFrequencyBinFactory: DominantFrequencyBinFactoryInterface = DominantFrequencyBinFactory(),
    private val hueFactory: HueFactoryInterface = HueFactory(),
    private val brightnessFactory: BrightnessFactoryInterface = BrightnessFactory()
) : ColorFactoryInterface {

    private val defaultColor = Color.black

    override fun create(audioSignal: AudioSignal): Color {
        val frequencyBins = getFrequencyBins(audioSignal)
        val bassBins = getBassBins(frequencyBins)
        val dominantBin = getDominantBin(bassBins) ?: return defaultColor
        return getColor(dominantBin)
    }

    private fun getFrequencyBins(audioSignal: AudioSignal): FrequencyBinList {
        return frequencyBinsService.getFrequencyBins(audioSignal)
    }

    private fun getBassBins(frequencyBins: FrequencyBinList): FrequencyBinList {
        return bassBinsFilter.filter(frequencyBins)
    }

    private fun getDominantBin(frequencyBins: FrequencyBinList): FrequencyBin? {
        return dominantFrequencyBinFactory.create(frequencyBins)
    }

    private fun getColor(dominantBin: FrequencyBin): Color {
        return Color.getHSBColor(
            getHue(dominantBin),
            getSaturation(),
            getBrightness(dominantBin)
        )
    }

    private fun getHue(dominantBin: FrequencyBin): Float {
        return hueFactory.create(dominantBin)
    }

    private fun getSaturation(): Float {
        return 1F
    }

    private fun getBrightness(dominantBin: FrequencyBin): Float {
        return brightnessFactory.create(dominantBin)
    }

}



package color

import color.dominantFrequency.DominantFrequencyBinFactory
import color.dominantFrequency.DominantFrequencyBinFactoryInterface
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.FrequencyBinsService
import sound.frequencyBins.FrequencyBinsServiceInterface
import sound.frequencyBins.filters.BassBinsFilter
import sound.frequencyBins.filters.BassBinsFilterInterface
import sound.frequencyBins.finders.FrequencyBinFinder
import sound.frequencyBins.finders.FrequencyBinFinderInterface
import sound.input.samples.AudioSignal
import java.awt.Color

interface ColorFactoryInterface {
    fun create(audioSignal: AudioSignal): Color
}

class ColorFactory(
    // TODO: Maybe there should be a BassBinsService? Creating AND filtering the bins here feels smelly.
    private val frequencyBinsService: FrequencyBinsServiceInterface = FrequencyBinsService(),
    private val bassBinsFilter: BassBinsFilterInterface = BassBinsFilter(),
    private val frequencyBinFinder: FrequencyBinFinderInterface = FrequencyBinFinder(),
    private val dominantFrequencyBinFactory: DominantFrequencyBinFactoryInterface = DominantFrequencyBinFactory(),
    private val hueFactory: HueFactoryInterface = HueFactory(),
    private val brightnessFactory: BrightnessFactoryInterface = BrightnessFactory()
) : ColorFactoryInterface {

    private val defaultColor = Color.black

    override fun create(audioSignal: AudioSignal): Color {
        val frequencyBins = getFrequencyBins(audioSignal)
        val bassBins = getBassBins(frequencyBins)
        val lowestBin = getLowestBin(bassBins) ?: return defaultColor
        val highestBin = getHighestBin(bassBins) ?: return defaultColor
        val dominantBin = getDominantBin(bassBins) ?: return defaultColor
        return getColor(dominantBin, lowestBin, highestBin)
    }

    private fun getFrequencyBins(audioSignal: AudioSignal): FrequencyBinList {
        return frequencyBinsService.getFrequencyBins(audioSignal)
    }

    private fun getBassBins(frequencyBins: FrequencyBinList): FrequencyBinList {
        return bassBinsFilter.filter(frequencyBins)
    }

    private fun getLowestBin(frequencyBins: FrequencyBinList): FrequencyBin? {
        return frequencyBinFinder.findLowest(frequencyBins)
    }

    private fun getHighestBin(frequencyBins: FrequencyBinList): FrequencyBin? {
        return frequencyBinFinder.findHighest(frequencyBins)
    }

    private fun getDominantBin(frequencyBins: FrequencyBinList): FrequencyBin? {
        return dominantFrequencyBinFactory.create(frequencyBins)
    }

    private fun getColor(dominantBin: FrequencyBin, lowestBin: FrequencyBin, highestBin: FrequencyBin): Color {
        return Color.getHSBColor(
            getHue(dominantBin, lowestBin, highestBin),
            getSaturation(),
            getBrightness(dominantBin)
        )
    }

    private fun getHue(dominantBin: FrequencyBin, lowestBin: FrequencyBin, highestBin: FrequencyBin): Float {
        return hueFactory.create(dominantBin, lowestBin, highestBin)
    }

    private fun getSaturation(): Float {
        return 1F
    }

    private fun getBrightness(dominantBin: FrequencyBin): Float {
        return brightnessFactory.create(dominantBin)
    }

}



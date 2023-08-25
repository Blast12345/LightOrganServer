package color

import input.audioFrame.AudioFrame
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.FrequencyBinsService
import sound.frequencyBins.dominantFrequency.DominantFrequencyBinFactory
import sound.frequencyBins.filters.BassFrequencyBinsFilter
import wrappers.color.Color

class ColorFactory(
    // TODO: Maybe there should be a BassBinsService? Creating AND filtering the bins here feels smelly.
    private val frequencyBinsService: FrequencyBinsService = FrequencyBinsService(),
    private val bassFrequencyBinsFilter: BassFrequencyBinsFilter = BassFrequencyBinsFilter(),
    private val dominantFrequencyBinFactory: DominantFrequencyBinFactory = DominantFrequencyBinFactory(),
    private val hueFactory: HueFactory = HueFactory(),
    private val brightnessFactory: BrightnessFactory = BrightnessFactory()
) {

    private val defaultColor = Color(0, 0, 0)

    fun create(audioFrame: AudioFrame): Color {
        val bins = getFrequencyBins(audioFrame)
        val bassBins = getBassFrequencyBins(bins)
        val dominantBin = getDominantFrequencyBin(bassBins) ?: return defaultColor
        println("Dominant: ${dominantBin.frequency}")
        return getColor(dominantBin)
    }

    private fun getFrequencyBins(audioFrame: AudioFrame): FrequencyBinList {
        return frequencyBinsService.getFrequencyBins(audioFrame)
    }

    private fun getBassFrequencyBins(frequencyBins: FrequencyBinList): FrequencyBinList {
        return bassFrequencyBinsFilter.filter(frequencyBins)
    }

    private fun getDominantFrequencyBin(frequencyBins: FrequencyBinList): FrequencyBin? {
        return dominantFrequencyBinFactory.create(frequencyBins)
    }

    private fun getColor(frequencyBin: FrequencyBin): Color {
        // TODO: Create a normalized color? 0,255,255 is roughly two times brighter than 0,255,255
        // This may solve some of the "flickeriness"
        return Color(
            hue = getHue(frequencyBin),
            saturation = getSaturation(),
            brightness = getBrightness(frequencyBin)
        )
    }

    private fun getHue(frequencyBin: FrequencyBin): Float {
        return hueFactory.create(frequencyBin)
    }

    private fun getSaturation(): Float {
        return 1F
    }

    private fun getBrightness(frequencyBin: FrequencyBin): Float {
        return brightnessFactory.create(frequencyBin)
    }

}



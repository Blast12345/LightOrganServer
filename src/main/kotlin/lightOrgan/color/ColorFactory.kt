package lightOrgan.color

import lightOrgan.sound.frequencyBins.FrequencyBin
import lightOrgan.sound.frequencyBins.FrequencyBinList
import lightOrgan.sound.frequencyBins.FrequencyBinsService
import lightOrgan.sound.frequencyBins.FrequencyBinsServiceInterface
import lightOrgan.sound.frequencyBins.dominantFrequency.DominantFrequencyBinFactory
import lightOrgan.sound.frequencyBins.dominantFrequency.DominantFrequencyBinFactoryInterface
import lightOrgan.sound.frequencyBins.filters.BassFrequencyBinsFilter
import lightOrgan.sound.frequencyBins.filters.BassFrequencyBinsFilterInterface
import lightOrgan.sound.input.samples.AudioSignal
import java.awt.Color

interface ColorFactoryInterface {
    fun create(audioSignal: AudioSignal): Color
}

class ColorFactory(
    // TODO: Maybe there should be a BassBinsService? Creating AND filtering the bins here feels smelly.
    private val frequencyBinsService: FrequencyBinsServiceInterface = FrequencyBinsService(),
    private val bassFrequencyBinsFilter: BassFrequencyBinsFilterInterface = BassFrequencyBinsFilter(),
    private val dominantFrequencyBinFactory: DominantFrequencyBinFactoryInterface = DominantFrequencyBinFactory(),
    private val hueFactory: HueFactoryInterface = HueFactory(),
    private val brightnessFactory: BrightnessFactoryInterface = BrightnessFactory()
) : ColorFactoryInterface {

    private val defaultColor = Color.black

    override fun create(audioSignal: AudioSignal): Color {
        val bins = getFrequencyBins(audioSignal)
        val bassBins = getBassFrequencyBins(bins)
        val dominantBin = getDominantFrequencyBin(bassBins) ?: return defaultColor
        println("Dominant: ${dominantBin.frequency}")
        return getColor(dominantBin)
    }

    private fun getFrequencyBins(audioSignal: AudioSignal): FrequencyBinList {
        return frequencyBinsService.getFrequencyBins(audioSignal)
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
        return Color.getHSBColor(
            getHue(frequencyBin),
            getSaturation(),
            getBrightness(frequencyBin)
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



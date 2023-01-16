package color

import config.Config
import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import sound.frequencyBins.FrequencyBinsService
import sound.frequencyBins.FrequencyBinsServiceInterface
import sound.frequencyBins.dominantFrequency.DominantFrequencyBinFactory
import sound.frequencyBins.dominantFrequency.DominantFrequencyBinFactoryInterface
import sound.frequencyBins.filters.BassFrequencyBinsFilter
import sound.frequencyBins.filters.BassFrequencyBinsFilterInterface
import sound.input.samples.AudioSignal
import java.awt.Color

interface ColorFactoryInterface {
    fun create(audioSignal: AudioSignal): Color
}

class ColorFactory(
    config: Config,
    // TODO: Maybe there should be a BassBinsService? Creating AND filtering the bins here feels smelly.
    private val frequencyBinsService: FrequencyBinsServiceInterface = FrequencyBinsService(config),
    private val bassFrequencyBinsFilter: BassFrequencyBinsFilterInterface = BassFrequencyBinsFilter(config),
    private val dominantFrequencyBinFactory: DominantFrequencyBinFactoryInterface = DominantFrequencyBinFactory(config),
    private val hueFactory: HueFactoryInterface = HueFactory(config),
    private val brightnessFactory: BrightnessFactoryInterface = BrightnessFactory()
) : ColorFactoryInterface {

    private val defaultColor = Color.black

    override fun create(audioSignal: AudioSignal): Color {
        val bins = getFrequencyBins(audioSignal)
        val bassBins = getBassFrequencyBins(bins)
        val dominantBin = getDominantFrequencyBin(bassBins) ?: return defaultColor
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



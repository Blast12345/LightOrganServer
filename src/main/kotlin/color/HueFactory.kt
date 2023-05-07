package color

import config.ColorWheel
import config.Config
import config.ConfigSingleton
import sound.frequencyBins.FrequencyBin

class HueFactory(
    private val config: Config = ConfigSingleton
) {

    private val colorWheel: ColorWheel = config.colorWheel

    fun create(frequencyBin: FrequencyBin): Float {
        return getBaseHue(frequencyBin) + getHueOffset()
    }

    private fun getBaseHue(frequencyBin: FrequencyBin): Float {
        val frequencyRange = getFrequencyRange()
        val positionInRange = getPositionInRange(frequencyBin)
        return positionInRange / frequencyRange
    }

    private fun getFrequencyRange(): Float {
        return colorWheel.endingFrequency - colorWheel.startingFrequency
    }

    private fun getPositionInRange(frequencyBin: FrequencyBin): Float {
        return frequencyBin.frequency - colorWheel.startingFrequency
    }

    private fun getHueOffset(): Float {
        return colorWheel.offset
    }

}
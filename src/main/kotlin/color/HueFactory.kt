package color

import ConfigSingleton
import config.children.ColorWheel
import sound.frequencyBins.FrequencyBin

class HueFactory(
    private val colorWheel: ColorWheel = ConfigSingleton.colorWheel
) {

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
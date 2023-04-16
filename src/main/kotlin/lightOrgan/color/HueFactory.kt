package lightOrgan.color

import config.Config
import config.ConfigSingleton
import config.children.ColorWheel
import lightOrgan.sound.frequencyBins.FrequencyBin

interface HueFactoryInterface {
    fun create(frequencyBin: FrequencyBin): Float
}

class HueFactory(
    private val config: Config = ConfigSingleton
) : HueFactoryInterface {

    private val colorWheel: ColorWheel = config.colorWheel

    override fun create(frequencyBin: FrequencyBin): Float {
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
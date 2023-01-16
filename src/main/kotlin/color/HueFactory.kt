package color

import Config
import sound.frequencyBins.FrequencyBin

interface HueFactoryInterface {
    fun create(dominantBin: FrequencyBin): Float
}

class HueFactory(
    private val colorWheel: ColorWheel = Config().colorWheel
) : HueFactoryInterface {

    override fun create(dominantBin: FrequencyBin): Float {
        return getBaseHue(dominantBin) + getHueOffset()
    }

    private fun getBaseHue(dominantBin: FrequencyBin): Float {
        val frequencyRange = getFrequencyRange()
        val positionInRange = getPositionInRange(dominantBin)
        return positionInRange / frequencyRange
    }

    private fun getFrequencyRange(): Float {
        return colorWheel.endingFrequency - colorWheel.startingFrequency
    }

    private fun getPositionInRange(dominantBin: FrequencyBin): Float {
        return dominantBin.frequency - colorWheel.startingFrequency
    }

    private fun getHueOffset(): Float {
        return colorWheel.offset
    }

}
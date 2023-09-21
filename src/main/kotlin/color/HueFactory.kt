package color

import config.ConfigSingleton
import config.children.ColorWheel

class HueFactory(
    private val colorWheel: ColorWheel = ConfigSingleton.colorWheel
) {

    fun create(frequency: Float): Float {
        return getBaseHue(frequency) + getHueOffset()
    }

    private fun getBaseHue(frequency: Float): Float {
        val frequencyRange = getFrequencyRange()
        val positionInRange = getPositionInRange(frequency)
        return positionInRange / frequencyRange
    }

    private fun getFrequencyRange(): Float {
        return colorWheel.endingFrequency - colorWheel.startingFrequency
    }

    private fun getPositionInRange(frequency: Float): Float {
        return frequency - colorWheel.startingFrequency
    }

    private fun getHueOffset(): Float {
        return colorWheel.offset
    }

}
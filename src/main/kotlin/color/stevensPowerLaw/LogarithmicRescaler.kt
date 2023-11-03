package color.stevensPowerLaw

import kotlin.math.log
import kotlin.math.pow

class LogarithmicRescaler {

    fun rescale(value: Float, fromScale: LogarithmicScale, toScale: LogarithmicScale): Float {
        val position = getPosition(value, fromScale)
        val newWidth = toScale.maximum.pow(toScale.power) - toScale.minimum.pow(toScale.power)
        return newWidth * position
    }

    private fun getPosition(value: Float, scale: LogarithmicScale): Float {
        val relativePosition = log(value, scale.power) - log(scale.minimum, scale.power)
        val width = log(scale.maximum, scale.power) - log(scale.minimum, scale.power)
        return (relativePosition / width) % 1
    }

}
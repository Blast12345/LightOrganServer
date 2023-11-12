package math

import kotlin.math.pow

class LogarithmicRescaler(
    private val normalization: LogarithmicMinMaxNormalization = LogarithmicMinMaxNormalization()
) {

    fun rescale(value: Float, fromScale: LogarithmicScale, toScale: LogarithmicScale): Float {
        val normalizedValue = calculateNormalizedValue(value, fromScale)
        val newScaleWidth = toScale.maximum.pow(toScale.power) - toScale.minimum.pow(toScale.power)

        return normalizedValue * newScaleWidth
    }

    private fun calculateNormalizedValue(value: Float, scale: LogarithmicScale): Float {
        return normalization.calculate(
            value = value,
            minimum = scale.minimum,
            maximum = scale.maximum,
            base = scale.power
        )
    }

}
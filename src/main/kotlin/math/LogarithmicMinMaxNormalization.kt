package math

import kotlin.math.log

class LogarithmicMinMaxNormalization(
    private val minMaxNormalization: MinMaxNormalization = MinMaxNormalization()
) {

    fun calculate(value: Float, minimum: Float, maximum: Float, base: Float): Float {
        return minMaxNormalization.calculate(
            value = log(value, base),
            minimum = log(minimum, base),
            maximum = log(maximum, base)
        )
    }

}
package math.featureScaling

import kotlin.math.log
import kotlin.math.pow

open class LogarithmicScale(
    private val minimum: Float,
    private val maximum: Float,
    private val base: Float
) {

    private val normalizer = LinearScale(
        minimum = log(minimum, base),
        maximum = log(maximum, base)
    )

    fun normalize(value: Float): Float {
        return normalizer.normalize(
            value = log(value, base)
        )
    }

    fun scale(value: Float): Float {
        return base.pow(normalizer.scale(value))
    }

}

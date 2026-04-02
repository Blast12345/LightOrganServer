package dsp.filtering

import kotlin.math.pow

sealed class FilterFamily {
    abstract fun rolloffRatio(dBFS: Float): Float

    data class Butterworth(val order: FilterOrder) : FilterFamily() {
        override fun rolloffRatio(dBFS: Float): Float {
            val target = 10.0.pow(dBFS / 20.0)
            return (1.0 / target.pow(2) - 1.0).pow(1.0 / (2 * order.value)).toFloat()
        }
    }

}
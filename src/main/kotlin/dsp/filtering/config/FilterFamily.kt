package dsp.filtering.config

import kotlin.math.pow

sealed class FilterFamily {
    abstract fun rolloffRatio(magnitudeDb: Float): Float

    data class Butterworth(val order: FilterOrder) : FilterFamily() {
        // TODO: Test me?
        override fun rolloffRatio(magnitudeDb: Float): Float {
            val target = 10.0.pow(magnitudeDb / 20.0)
            return (1.0 / target.pow(2) - 1.0).pow(1.0 / (2 * order.value)).toFloat()
        }
    }

}
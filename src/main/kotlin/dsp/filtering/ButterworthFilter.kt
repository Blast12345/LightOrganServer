package dsp.filtering

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow

object ButterworthFilter {

    fun buildStages(
        frequency: Float,
        order: Int,
        sampleRate: Float,
        firstOrderFactory: (Double, Double) -> OnePoleOneZeroFilter,
        biquadraticFactory: (Double, Double, Double) -> BiquadraticFilter,
    ): List<Filter> {
        require(order >= 1) { "Filter order must be at least 1" }

        val stages = mutableListOf<Filter>()
        val hasUnpairedPole = order % 2 != 0

        if (hasUnpairedPole) {
            stages.add(firstOrderFactory(frequency.toDouble(), sampleRate.toDouble()))
        }

        for (k in 0 until order / 2) {
            val poleIndex = if (hasUnpairedPole) k + 1 else k
            val q = q(order, poleIndex)
            stages.add(biquadraticFactory(frequency.toDouble(), sampleRate.toDouble(), q))
        }

        return stages
    }

    fun q(order: Int, stageIndex: Int): Double {
        val pole = PI * (2 * stageIndex + 1) / (2.0 * order)
        return 1.0 / (2.0 * cos(pole))
    }

    fun rolloffRatio(magnitudeDb: Float, order: Int): Float {
        val target = 10.0.pow(magnitudeDb / 20.0)
        return (1.0 / target.pow(2) - 1.0).pow(1.0 / (2 * order)).toFloat()
    }

}
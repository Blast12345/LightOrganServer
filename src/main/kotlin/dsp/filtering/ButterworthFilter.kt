package dsp.filtering

import kotlin.math.PI
import kotlin.math.cos

object ButterworthFilter {

    fun buildStages(
        frequency: Float,
        order: Int,
        sampleRate: Float,
        firstOrderFactory: (Double, Double) -> OnePoleOneZeroFilter,
        biquadraticFactory: (Double, Double, Double) -> BiquadraticFilter,
    ): List<SampleFilter> {
        require(order >= 1) { "Filter order must be at least 1" }

        val stages = mutableListOf<SampleFilter>()
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

}
package dsp.filtering

import kotlin.math.PI
import kotlin.math.cos

object ButterworthFilter {

    // Topologies
    fun lowPass(frequency: Float, order: Int, sampleRate: Float): CascadedFilter {
        return buildFilter(frequency, order, sampleRate, OnePoleOneZeroFilter::lowPass, BiquadraticFilter::lowPass)
    }

    fun highPass(frequency: Float, order: Int, sampleRate: Float): CascadedFilter {
        return buildFilter(frequency, order, sampleRate, OnePoleOneZeroFilter::highPass, BiquadraticFilter::highPass)
    }

    // Construction
    private fun buildFilter(
        frequency: Float,
        order: Int,
        sampleRate: Float,
        firstOrderFactory: (Double, Double) -> OnePoleOneZeroFilter,
        biquadraticFactory: (Double, Double, Double) -> BiquadraticFilter,
    ): CascadedFilter {
        require(order >= 1) { "Filter order must be at least 1" }

        val stages = mutableListOf<SampleFilter>()
        val hasUnpairedPole = order % 2 != 0
        val numberOfBiquadraticStages = order / 2

        if (hasUnpairedPole) {
            val stage = firstOrderFactory(frequency.toDouble(), sampleRate.toDouble())
            stages.add(stage)
        }

        for (stageNumber in 0 until numberOfBiquadraticStages) {
            val poleIndex = if (hasUnpairedPole) stageNumber + 1 else stageNumber
            val q = q(order, poleIndex)
            val stage = biquadraticFactory(frequency.toDouble(), sampleRate.toDouble(), q)

            stages.add(stage)
        }

        return CascadedFilter(sampleRate, stages)
    }

    private fun q(order: Int, stageIndex: Int): Double {
        val pole = PI * (2 * stageIndex + 1) / (2.0 * order)
        return 1.0 / (2.0 * cos(pole))
    }

}
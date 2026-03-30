package dsp.filtering

class ButterworthLowPass(
    override val cutoffFrequency: Float,
    order: Int,
    sampleRate: Float,
) : CascadedFilter(
    sampleRate = sampleRate,
    stages = ButterworthFilter.buildStages(
        cutoffFrequency, order, sampleRate,
        OnePoleOneZeroFilter::lowPass,
        BiquadraticFilter::lowPass,
    ),
), LowPassFilter {

    // TODO: Test me
    override fun frequencyAtMagnitude(magnitudeDb: Float): Float {
        val ratio = ButterworthFilter.rolloffRatio(magnitudeDb, order)
        return cutoffFrequency * ratio
    }

}
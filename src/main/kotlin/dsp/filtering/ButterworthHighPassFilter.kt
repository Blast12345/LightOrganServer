package dsp.filtering

class ButterworthHighPass(
    override val cutoffFrequency: Float,
    order: Int,
    sampleRate: Float,
) : CascadedFilter(
    sampleRate = sampleRate,
    stages = ButterworthFilter.buildStages(
        cutoffFrequency, order, sampleRate,
        OnePoleOneZeroFilter::highPass,
        BiquadraticFilter::highPass,
    ),
), HighPassFilter {

    // TODO: Test me
    override fun frequencyAtMagnitude(magnitudeDb: Float): Float {
        val ratio = ButterworthFilter.rolloffRatio(magnitudeDb, order)
        return cutoffFrequency / ratio
    }

}
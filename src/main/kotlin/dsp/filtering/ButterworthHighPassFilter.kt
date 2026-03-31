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
), HighPassFilter
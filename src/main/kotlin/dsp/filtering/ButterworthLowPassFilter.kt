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
), LowPassFilter
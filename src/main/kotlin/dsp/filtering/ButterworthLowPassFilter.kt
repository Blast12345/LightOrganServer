package dsp.filtering

class ButterworthLowPass(
    frequency: Float,
    order: Int,
    sampleRate: Float,
) : CascadedFilter(
    sampleRate = sampleRate,
    stages = ButterworthFilter.buildStages(
        frequency, order, sampleRate,
        OnePoleOneZeroFilter::lowPass,
        BiquadraticFilter::lowPass,
    ),
)
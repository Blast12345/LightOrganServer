package dsp.filtering

class ButterworthHighPass(
    frequency: Float,
    order: Int,
    sampleRate: Float,
) : CascadedFilter(
    sampleRate = sampleRate,
    stages = ButterworthFilter.buildStages(
        frequency, order, sampleRate,
        OnePoleOneZeroFilter::highPass,
        BiquadraticFilter::highPass,
    ),
)
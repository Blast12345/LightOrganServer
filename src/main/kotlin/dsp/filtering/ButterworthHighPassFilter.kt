package dsp.filtering

class ButterworthHighPass(
    frequency: Float,
    order: Int,
    sampleRate: Float,
) : CascadedFilter(
    supportedSampleRate = sampleRate,
    stages = ButterworthFilter.buildStages(
        frequency, order, sampleRate,
        OnePoleOneZeroFilter::highPass,
        BiquadraticFilter::highPass,
    ),
)
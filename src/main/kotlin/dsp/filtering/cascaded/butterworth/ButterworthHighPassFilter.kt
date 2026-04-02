package dsp.filtering.cascaded.butterworth

import dsp.filtering.cascaded.CascadedFilter
import dsp.filtering.primitives.BiquadraticFilter
import dsp.filtering.primitives.OnePoleOneZeroFilter

class ButterworthHighPass(
    val cutoffFrequency: Float,
    order: Int,
    sampleRate: Float,
) : CascadedFilter(
    sampleRate = sampleRate,
    stages = ButterworthFilter.buildStages(
        cutoffFrequency, order, sampleRate,
        OnePoleOneZeroFilter.Companion::highPass,
        BiquadraticFilter.Companion::highPass,
    ),
)
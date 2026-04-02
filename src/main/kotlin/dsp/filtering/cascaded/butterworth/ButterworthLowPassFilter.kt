package dsp.filtering.cascaded.butterworth

import dsp.filtering.cascaded.CascadedFilter
import dsp.filtering.primitives.BiquadraticFilter
import dsp.filtering.primitives.OnePoleOneZeroFilter

class ButterworthLowPass(
    val cutoffFrequency: Float,
    order: Int,
    sampleRate: Float,
) : CascadedFilter(
    sampleRate = sampleRate,
    stages = ButterworthFilter.buildStages(
        cutoffFrequency, order, sampleRate,
        OnePoleOneZeroFilter.Companion::lowPass,
        BiquadraticFilter.Companion::lowPass,
    ),
)
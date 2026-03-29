package dsp.filtering.config

import dsp.filtering.ButterworthHighPass
import dsp.filtering.ButterworthLowPass
import dsp.filtering.HighPassFilter
import dsp.filtering.LowPassFilter

class FilterBuilder {

    fun build(config: FilterConfig.HighPass, sampleRate: Float): HighPassFilter {
        return when (val family = config.family) {
            is FilterFamily.Butterworth -> ButterworthHighPass(config.frequency, family.order.value, sampleRate)
        }
    }

    fun build(config: FilterConfig.LowPass, sampleRate: Float): LowPassFilter {
        return when (val family = config.family) {
            is FilterFamily.Butterworth -> ButterworthLowPass(config.frequency, family.order.value, sampleRate)
        }
    }

}
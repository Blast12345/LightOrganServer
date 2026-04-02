package dsp.filtering

import dsp.filtering.cascaded.butterworth.ButterworthHighPass
import dsp.filtering.cascaded.butterworth.ButterworthLowPass

class FilterBuilder {

    fun build(config: FilterConfig, sampleRate: Float): Filter {
        return when (config.family) {
            is FilterFamily.Butterworth -> buildButterworth(config.type, config.family, sampleRate)
        }
    }

    private fun buildButterworth(
        type: FilterType,
        family: FilterFamily.Butterworth,
        sampleRate: Float
    ): Filter {
        val order = family.order.value
        return when (type) {
            is FilterType.LowPass -> ButterworthLowPass(type.frequency, order, sampleRate)
            is FilterType.HighPass -> ButterworthHighPass(type.frequency, order, sampleRate)
        }
    }

}
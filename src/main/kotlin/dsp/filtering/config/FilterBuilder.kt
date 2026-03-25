package dsp.filtering.config

import dsp.filtering.ButterworthHighPass
import dsp.filtering.ButterworthLowPass
import dsp.filtering.CascadedFilter
import dsp.filtering.Filter

class FilterBuilder {

    fun build(config: FilterConfig, sampleRate: Float): Filter {
        return when (config.family) {
            is FilterFamily.Butterworth -> buildButterworth(config.topology, config.family.order, sampleRate)
        }
    }

    private fun buildButterworth(
        topology: FilterTopology, order: FilterOrder, sampleRate: Float
    ): CascadedFilter {
        return when (topology) {
            is FilterTopology.LowPass -> ButterworthLowPass(topology.frequency, order.value, sampleRate)
            is FilterTopology.HighPass -> ButterworthHighPass(topology.frequency, order.value, sampleRate)
        }
    }

}
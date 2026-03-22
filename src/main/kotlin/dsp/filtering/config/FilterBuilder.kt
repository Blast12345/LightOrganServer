package dsp.filtering.config

import dsp.filtering.ButterworthHighPass
import dsp.filtering.ButterworthLowPass
import dsp.filtering.CascadedFilter
import dsp.filtering.OrderedFilter

class FilterBuilder {

    fun build(config: FilterConfig, sampleRate: Float): OrderedFilter {
        return when (config.family) {
            FilterFamily.BUTTERWORTH -> buildButterworth(config.topology, config.order, sampleRate)
        }
    }

    private fun buildButterworth(topology: FilterTopology, order: Int, sampleRate: Float): CascadedFilter {
        return when (topology) {
            is FilterTopology.LowPass -> ButterworthLowPass(topology.frequency, order, sampleRate)
            is FilterTopology.HighPass -> ButterworthHighPass(topology.frequency, order, sampleRate)
        }
    }

}
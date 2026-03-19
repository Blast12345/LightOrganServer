package dsp.filtering.config

import dsp.filtering.ButterworthFilter
import dsp.filtering.CascadedFilter
import dsp.filtering.SampleFilter

object FilterBuilder {

    fun build(config: FilterConfig, sampleRate: Float): SampleFilter {
        return when (config.family) {
            FilterFamily.BUTTERWORTH -> buildButterworth(config.topology, config.order, sampleRate)
        }
    }

    private fun buildButterworth(topology: FilterTopology, order: Int, sampleRate: Float): CascadedFilter {
        return when (topology) {
            is FilterTopology.LowPass -> ButterworthFilter.lowPass(topology.frequency, order, sampleRate)
            is FilterTopology.HighPass -> ButterworthFilter.highPass(topology.frequency, order, sampleRate)
        }
    }

}
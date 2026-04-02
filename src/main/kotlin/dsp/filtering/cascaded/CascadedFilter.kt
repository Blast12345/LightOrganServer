package dsp.filtering.cascaded

import dsp.filtering.Filter

abstract class CascadedFilter(
    override val sampleRate: Float,
    private val stages: List<Filter>
) : Filter {

    override val order = stages.sumOf { it.order }

    override fun filter(samples: FloatArray): FloatArray {
        return stages.fold(samples) { signal, stage -> stage.filter(signal) }
    }

}
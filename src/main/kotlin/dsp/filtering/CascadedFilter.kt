package dsp.filtering

abstract class CascadedFilter(
    override val sampleRate: Float,
    private val stages: List<OrderedFilter>
) : OrderedFilter {

    override val order: Int = stages.sumOf { it.order }

    override fun filter(samples: FloatArray): FloatArray {
        return stages.fold(samples) { signal, stage -> stage.filter(signal) }
    }

}
package dsp.filtering

class CascadedFilter(
    private val stages: List<SampleFilter>
) : SampleFilter {

    override fun filter(samples: FloatArray): FloatArray {
        return stages.fold(samples) { signal, stage -> stage.filter(signal) }
    }

    override fun reset() {
        stages.forEach { it.reset() }
    }

}
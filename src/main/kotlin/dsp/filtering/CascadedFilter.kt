package dsp.filtering

class CascadedFilter(
    override val supportedSampleRate: Float,
    private val stages: List<SampleFilter>
) : SampleFilter {

    override fun filter(samples: FloatArray): FloatArray {
        return stages.fold(samples) { signal, stage -> stage.filter(signal) }
    }

}
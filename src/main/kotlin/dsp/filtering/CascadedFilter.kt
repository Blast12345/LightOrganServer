package dsp.filtering

abstract class CascadedFilter(
    override val sampleRate: Float,
    private val stages: List<Filter>
) : Filter {

    override val order = stages.sumOf { it.order }

    override fun filter(samples: FloatArray): FloatArray {
        return stages.fold(samples) { signal, stage -> stage.filter(signal) }
    }

    override fun magnitudeAt(frequency: Float): Float {
        return stages.fold(1f) { magnitude, stage ->
            magnitude * stage.magnitudeAt(frequency)
        }
    }

}
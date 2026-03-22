package dsp.filtering.config

// Add more filter types (bandpass, notch, etc.) as needed.
sealed class FilterTopology {
    data class LowPass(val frequency: Float) : FilterTopology()
    data class HighPass(val frequency: Float) : FilterTopology()
}
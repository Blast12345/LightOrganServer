package dsp.filtering.config

// ENHANCEMENT: Add more filter types (bandpass, notch, etc.)
sealed class FilterTopology {
    data class LowPass(val frequency: Float) : FilterTopology()
    data class HighPass(val frequency: Float) : FilterTopology()
}
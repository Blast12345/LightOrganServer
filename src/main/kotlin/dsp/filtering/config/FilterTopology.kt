package dsp.filtering.config

sealed class FilterTopology {
    data class LowPass(val frequency: Float) : FilterTopology()
    data class HighPass(val frequency: Float) : FilterTopology()
    // ... bandpass, notch, etc
}
package dsp.filtering

sealed class FilterType {
    data class LowPass(val frequency: Float) : FilterType()
    data class HighPass(val frequency: Float) : FilterType()
}
package dsp.filtering.config

sealed class FilterFamily {
    data class Butterworth(val order: FilterOrder) : FilterFamily()
}
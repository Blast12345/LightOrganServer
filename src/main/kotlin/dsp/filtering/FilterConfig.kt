package dsp.filtering

data class FilterConfig(
    val type: FilterType,
    val family: FilterFamily,
) {

    fun frequencyAt(dBFS: Float): Float {
        val ratio = family.rolloffRatio(dBFS)

        return when (type) {
            is FilterType.LowPass -> type.frequency * ratio
            is FilterType.HighPass -> type.frequency / ratio
        }
    }

}
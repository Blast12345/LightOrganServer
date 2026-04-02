package dsp.filtering

data class FilterConfig(
    val type: FilterType,
    val family: FilterFamily,
) {

    fun frequencyAtMagnitude(magnitudeDb: Float): Float {
        val ratio = family.rolloffRatio(magnitudeDb)

        return when (type) {
            is FilterType.LowPass -> type.frequency * ratio
            is FilterType.HighPass -> type.frequency / ratio
        }
    }

}
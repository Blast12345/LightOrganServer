package dsp.filtering.config

sealed class FilterConfig {
    abstract val family: FilterFamily

    data class HighPass(
        override val family: FilterFamily,
        val frequency: Float
    ) : FilterConfig() {
        fun frequencyAtMagnitude(magnitudeDb: Float): Float {
            return frequency / family.rolloffRatio(magnitudeDb)
        }
    }

    data class LowPass(
        override val family: FilterFamily,
        val frequency: Float
    ) : FilterConfig() {
        fun frequencyAtMagnitude(magnitudeDb: Float): Float {
            return frequency * family.rolloffRatio(magnitudeDb)
        }
    }
    
}
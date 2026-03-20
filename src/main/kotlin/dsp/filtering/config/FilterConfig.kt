package dsp.filtering.config

data class FilterConfig(
    val family: FilterFamily,
    val topology: FilterTopology,
    val order: Int,
) {

    companion object {

        fun highPass(family: FilterFamily, frequency: Float, order: Int): FilterConfig {
            return FilterConfig(family, FilterTopology.HighPass(frequency), order)
        }

        fun lowPass(family: FilterFamily, frequency: Float, order: Int): FilterConfig {
            return FilterConfig(family, FilterTopology.LowPass(frequency), order)
        }

        fun highPassFromSlope(family: FilterFamily, frequency: Float, dbPerOctave: Int): FilterConfig {
            return highPass(family, frequency, orderFromSlope(dbPerOctave))
        }

        fun lowPassFromSlope(family: FilterFamily, frequency: Float, dbPerOctave: Int): FilterConfig {
            return lowPass(family, frequency, orderFromSlope(dbPerOctave))
        }

        private fun orderFromSlope(dbPerOctave: Int): Int {
            require(dbPerOctave % 6 == 0) { "Slope must be a multiple of 6 dB/oct" }
            require(dbPerOctave > 0) { "Slope must be positive" }
            return dbPerOctave / 6
        }

    }
}
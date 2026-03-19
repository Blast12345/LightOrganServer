package dsp.filtering.config

data class FilterConfig(
    val family: FilterFamily,
    val topology: FilterTopology,
    val order: Int,
) {

    // Factory methods
    companion object {

        fun butterworthHighPass(frequency: Float, order: Int): FilterConfig {
            return FilterConfig(FilterFamily.BUTTERWORTH, FilterTopology.HighPass(frequency), order)
        }

        fun butterworthLowPass(frequency: Float, order: Int): FilterConfig {
            return FilterConfig(FilterFamily.BUTTERWORTH, FilterTopology.LowPass(frequency), order)
        }

        fun butterworthHighPassFromSlope(frequency: Float, dbPerOctave: Int): FilterConfig {
            return butterworthHighPass(frequency, orderFromSlope(dbPerOctave))
        }

        fun butterworthLowPassFromSlope(frequency: Float, dbPerOctave: Int): FilterConfig {
            return butterworthLowPass(frequency, orderFromSlope(dbPerOctave))
        }

        private fun orderFromSlope(dbPerOctave: Int): Int {
            require(dbPerOctave % 6 == 0) { "Slope must be a multiple of 6 dB/oct" }
            require(dbPerOctave > 0) { "Slope must be positive" }
            return dbPerOctave / 6
        }

    }
}
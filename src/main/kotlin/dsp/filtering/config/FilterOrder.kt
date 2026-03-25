package dsp.filtering.config

@JvmInline
value class FilterOrder(val value: Int) {

    init {
        require(value >= 1) { "Filter order must be at least 1" }
    }

    companion object {
        fun fromDbPerOctave(dbPerOctave: Int): FilterOrder {
            require(dbPerOctave > 0) { "Slope must be positive" }
            require(dbPerOctave % 6 == 0) { "Slope must be a multiple of 6 dB/oct" }
            return FilterOrder(dbPerOctave / 6)
        }
    }

}
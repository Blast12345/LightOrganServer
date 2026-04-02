package dsp.filtering

import kotlin.math.absoluteValue

@JvmInline
value class FilterOrder(val value: Int) {

    init {
        require(value >= 1) { "Filter order must be at least 1" }
    }

    companion object {
        fun fromDbPerOctave(dbPerOctave: Int): FilterOrder {
            require(dbPerOctave != 0) { "Slope must be non-zero" }
            require(dbPerOctave % 6 == 0) { "Slope must be a multiple of 6 dB/oct" }
            return FilterOrder(dbPerOctave.absoluteValue / 6)
        }
    }

}
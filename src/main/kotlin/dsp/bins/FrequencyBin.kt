package dsp.bins

import kotlin.math.abs

data class FrequencyBin(
    val frequency: Float,
    val magnitude: Float
)

typealias FrequencyBins = List<FrequencyBin>

fun FrequencyBins.nearestTo(frequency: Float): FrequencyBin {
    return minBy { abs(it.frequency - frequency) }
}

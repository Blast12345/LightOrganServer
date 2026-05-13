package dsp.bins

import kotlin.math.abs

// Reference: https://dsp.stackexchange.com/questions/8317/fft-amplitude-or-magnitude
// I'm using the term "magnitude" instead of "amplitude" because the value is inherently non-negative.
data class FrequencyBin(
    val frequency: Float,
    val magnitude: Float
)

typealias FrequencyBins = List<FrequencyBin>

fun FrequencyBins.nearestTo(frequency: Float): FrequencyBin {
    return minBy { abs(it.frequency - frequency) }
}

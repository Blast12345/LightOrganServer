package dsp.bins

import kotlin.math.abs

// Reference: https://dsp.stackexchange.com/questions/8317/fft-amplitude-or-magnitude
// I'm using the term "magnitude" instead of "amplitude" because the value is inherently non-negative.
data class FrequencyBin(
    val frequency: Float,
    val magnitude: Float
) {

    /**
     * Magnitude is proportional to sound pressure (pascals), normalized to digital full scale (0–1) rather than referenced to 20 µPa.
     * That is to say, magnitude is analogous sound pressure (not to be confused with SPL, which is a log scale).
     * Though this variable doesn't functionally do anything, it expresses the relationship.
     */
    val normalizedSoundPressure: Float get() = magnitude

}

typealias FrequencyBins = List<FrequencyBin>

fun FrequencyBins.nearestTo(frequency: Float): FrequencyBin {
    return minBy { abs(it.frequency - frequency) }
}

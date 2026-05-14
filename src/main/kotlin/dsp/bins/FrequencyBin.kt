package dsp.bins

import org.apache.commons.math3.complex.Complex
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


data class FrequencyBin(
    val frequency: Float,
    val value: Complex
) {

    // Reference: https://dsp.stackexchange.com/questions/8317/fft-amplitude-or-magnitude
    // I'm using the term "magnitude" instead of "amplitude" because the value is inherently non-negative.
    val magnitude: Float get() = value.abs().toFloat()

    constructor(frequency: Float, magnitude: Double, phase: Double = 0.0) : this(
        frequency = frequency,
        value = Complex(magnitude * cos(phase), magnitude * sin(phase))
    )

}

typealias FrequencyBins = List<FrequencyBin>

fun FrequencyBins.nearestTo(frequency: Float): FrequencyBin {
    return minBy { abs(it.frequency - frequency) }
}

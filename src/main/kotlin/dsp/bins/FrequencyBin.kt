package dsp.bins

import org.apache.commons.math3.complex.Complex
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

data class FrequencyBin(
    val frequency: Float,
    val value: Complex
) {

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

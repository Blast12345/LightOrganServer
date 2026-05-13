package dsp.bins

import org.apache.commons.math3.complex.Complex
import kotlin.math.abs

data class FrequencyBin(
    val frequency: Float,
    val value: Complex
) {

    val magnitude: Float get() = value.abs().toFloat()

}

typealias FrequencyBins = List<FrequencyBin>

fun FrequencyBins.nearestTo(frequency: Float): FrequencyBin {
    return minBy { abs(it.frequency - frequency) }
}

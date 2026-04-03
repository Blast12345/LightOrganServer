package math.physics

import kotlin.math.pow
import kotlin.math.sqrt

// TODO: Test me
// Reference: https://sengpielaudio.com/AddingAmplitudesAndLevels.pdf
// e.g. 80 dB + 80 dB = 83 dB
fun sumSoundPressure(magnitudes: List<Double>): Double {
    return sqrt(magnitudes.fold(0.0) { sum, m -> sum + m.pow(2) })
}

fun sumSoundPressure(magnitudes: List<Float>): Float {
    return sumSoundPressure(magnitudes.map { it.toDouble() }).toFloat()
}
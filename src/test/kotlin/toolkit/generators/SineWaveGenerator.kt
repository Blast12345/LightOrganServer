package toolkit.generators

import kotlin.math.PI
import kotlin.math.sin

fun generateSineWave(
    frequency: Float,
    amplitude: Float,
    sampleSize: Int,
    sampleRate: Float
): FloatArray {
    return FloatArray(sampleSize) { i ->
        amplitude * sin(2.0 * PI * frequency * i / sampleRate).toFloat()
    }
}
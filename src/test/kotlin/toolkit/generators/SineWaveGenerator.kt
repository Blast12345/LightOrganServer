package toolkit.generators

import kotlin.math.PI
import kotlin.math.sin
import kotlin.time.Duration

fun generateSineWave(
    frequency: Float,
    amplitude: Float,
    duration: Duration,
    sampleRate: Float
): WaveForm {
    val sampleSize = (sampleRate * duration.inWholeSeconds).toInt()

    return WaveForm(
        sampleRate = sampleRate,
        samples = FloatArray(sampleSize) { i -> amplitude * sin(2.0 * PI * frequency * i / sampleRate).toFloat() }
    )
}


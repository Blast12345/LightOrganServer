package toolkit.generators

import kotlin.math.PI
import kotlin.math.sin
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun generateSineWave(
    frequency: Float,
    sampleRate: Float,
    amplitude: Float = 1f,
    duration: Duration = 1.seconds,
): WaveForm {
    val sampleSize = (sampleRate * duration.inWholeSeconds).toInt()

    return WaveForm(
        sampleRate = sampleRate,
        samples = FloatArray(sampleSize) { i -> amplitude * sin(2.0 * PI * frequency * i / sampleRate).toFloat() }
    )
}
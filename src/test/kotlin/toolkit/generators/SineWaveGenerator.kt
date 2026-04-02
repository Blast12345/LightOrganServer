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

fun generateSilence(sampleRate: Float, duration: Duration = 1.seconds): WaveForm {
    return generateSineWave(frequency = 0f, sampleRate = sampleRate, amplitude = 0f, duration = duration)
}

fun combineWaves(vararg waves: WaveForm): WaveForm {
    require(waves.isNotEmpty())
    require(waves.all { it.sampleRate == waves[0].sampleRate })
    require(waves.all { it.samples.size == waves[0].samples.size })

    return WaveForm(
        sampleRate = waves[0].sampleRate,
        samples = FloatArray(waves[0].samples.size) { i ->
            waves.sumOf { it.samples[i].toDouble() }.toFloat()
        }
    )
}
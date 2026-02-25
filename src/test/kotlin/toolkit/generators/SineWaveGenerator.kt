package toolkit.generators

import audio.samples.AudioFormat
import audio.samples.AudioFrame
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

fun generateMonoAudioFrame(
    samples: FloatArray,
    sampleRate: Float,
): AudioFrame {
    return AudioFrame(
        samples = samples,
        format = AudioFormat(
            sampleRate = sampleRate,
            bitDepth = 16, // NOTE: Bit-depth only matters for raw bytes
            channels = 1
        )
    )
}

fun generateStereoAudioFrame(
    left: FloatArray,
    right: FloatArray,
    sampleRate: Float,
): AudioFrame {
    require(left.size == right.size)

    val interleaved = FloatArray(left.size * 2) { i ->
        if (i % 2 == 0) left[i / 2] else right[i / 2]
    }

    return AudioFrame(
        samples = interleaved,
        format = AudioFormat(
            sampleRate = sampleRate,
            bitDepth = 16, // NOTE: Bit-depth only matters for raw bytes
            channels = 2
        )
    )
}
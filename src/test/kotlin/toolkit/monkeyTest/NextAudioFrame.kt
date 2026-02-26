package toolkit.monkeyTest

import audio.samples.AudioFormat
import audio.samples.AudioFrame

fun nextAudioFrame(
    channels: List<FloatArray> = listOf(nextFloatArray()),
    sampleRate: Float = nextPositiveInt().toFloat(),
    bitDepth: Int = nextPositiveInt()
): AudioFrame {
    require(channels.isNotEmpty())
    val frameSize = channels[0].size
    require(channels.all { it.size == frameSize })

    val interleaved = FloatArray(frameSize * channels.size) { i ->
        channels[i % channels.size][i / channels.size]
    }

    return AudioFrame(
        samples = interleaved,
        format = AudioFormat(
            sampleRate = sampleRate,
            bitDepth = bitDepth,
            channels = channels.size
        )
    )
}
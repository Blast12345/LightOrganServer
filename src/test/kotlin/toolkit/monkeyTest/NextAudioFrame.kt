package toolkit.monkeyTest

import audio.samples.AudioFormat
import audio.samples.AudioFrame
import toolkit.generators.WaveForm

fun nextAudioFrame(
    channels: List<FloatArray> = listOf(nextFloatArray()),
    sampleRate: Float = nextPositiveFloat(),
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

fun nextAudioFrame(
    vararg channels: WaveForm
): AudioFrame {
    require(channels.isNotEmpty()) { "Must provide at least one channel" }
    require(channels.all { it.sampleRate == channels[0].sampleRate }) { "Sample rates must match" }
    require(channels.all { it.samples.size == channels[0].samples.size }) { "Sample counts must match" }

    return nextAudioFrame(
        channels = channels.map { it.samples },
        sampleRate = channels[0].sampleRate
    )
}
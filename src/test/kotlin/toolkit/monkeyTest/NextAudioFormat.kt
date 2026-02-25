package toolkit.monkeyTest

import audio.samples.AudioFormat

fun nextAudioFormat(
    sampleRate: Float = nextPositiveInt().toFloat(),
    channels: Int = nextPositiveInt()
): AudioFormat {
    return AudioFormat(
        sampleRate = sampleRate,
        bitDepth = nextPositiveInt(),
        channels = channels
    )
}

package toolkit.monkeyTest

import input.AudioFormat

fun nextAudioFormat(
    sampleRate: Int = nextPositiveInt(),
    channels: Int = nextPositiveInt()
): AudioFormat {
    return AudioFormat(
        sampleRate = sampleRate,
        bitDepth = nextPositiveInt(),
        channels = channels
    )
}

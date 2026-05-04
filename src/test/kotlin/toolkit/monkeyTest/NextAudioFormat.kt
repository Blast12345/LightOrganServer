package toolkit.monkeyTest

import audio.samples.AudioFormat

fun nextAudioFormat(
    sampleRate: Float = nextPositiveFloat(),
    channels: Int = nextPositiveInt()
): AudioFormat {
    return AudioFormat(
        sampleRate = sampleRate,
        bitDepth = nextPositiveInt(),
        channels = channels
    )
}

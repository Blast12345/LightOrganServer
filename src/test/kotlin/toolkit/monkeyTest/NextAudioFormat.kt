package toolkit.monkeyTest

import audio.samples.AudioFormat

fun nextAudioFormat(
    sampleRate: Float = nextPositiveFloat(),
    channels: Int = nextInt()
): AudioFormat {
    return AudioFormat(
        sampleRate = sampleRate,
        bitDepth = nextInt(),
        channels = channels
    )
}

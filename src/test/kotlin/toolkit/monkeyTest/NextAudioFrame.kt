package toolkit.monkeyTest

import audio.samples.AudioFrame

fun nextAudioFrame(
    sampleRate: Int = nextPositiveInt()
): AudioFrame {
    return AudioFrame(
        samples = nextFloatArray(),
        format = nextAudioFormat(sampleRate)
    )
}
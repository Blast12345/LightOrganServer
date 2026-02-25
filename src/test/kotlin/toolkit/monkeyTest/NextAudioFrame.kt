package toolkit.monkeyTest

import audio.samples.AudioFrame

fun nextAudioFrame(
    sampleRate: Float = nextPositiveInt().toFloat()
): AudioFrame {
    return AudioFrame(
        samples = nextFloatArray(),
        format = nextAudioFormat(sampleRate)
    )
}
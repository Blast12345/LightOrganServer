package toolkit.monkeyTest

import audio.samples.AudioFrame

fun nextAudioFrame(): AudioFrame {
    return AudioFrame(
        samples = nextFloatArray(),
        format = nextAudioFormat()
    )
}
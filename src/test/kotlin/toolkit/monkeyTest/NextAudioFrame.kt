package toolkit.monkeyTest

import input.samples.AudioFrame

fun nextAudioFrame(): AudioFrame {
    return AudioFrame(
        samples = nextFloatArray(),
        format = nextAudioFormat()
    )
}
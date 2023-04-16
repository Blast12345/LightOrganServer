package toolkit.monkeyTest

import input.audioFrame.AudioFrame

fun nextAudioFrame(samples: DoubleArray = nextDoubleArray()): AudioFrame {
    return AudioFrame(
        samples = samples,
        format = nextAudioFormatWrapper()
    )
}
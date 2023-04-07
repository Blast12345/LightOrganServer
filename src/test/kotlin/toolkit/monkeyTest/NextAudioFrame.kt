package toolkit.monkeyTest

import lightOrgan.sound.input.samples.AudioSignal

fun nextAudioSignal(samples: DoubleArray = nextDoubleArray()): AudioSignal {
    return AudioSignal(
        samples = samples,
        format = nextAudioFormatWrapper()
    )
}
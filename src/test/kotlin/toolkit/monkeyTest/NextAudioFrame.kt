package toolkit.monkeyTest

import input.samples.AudioSignal

fun nextAudioSignal(samples: DoubleArray = nextDoubleArray()): AudioSignal {
    return AudioSignal(
        samples = samples,
        format = nextAudioFormatWrapper()
    )
}
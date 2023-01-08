package toolkit.monkeyTest

import sound.input.samples.AudioSignal
import kotlin.random.Random

fun nextAudioSignal(samples: DoubleArray = nextDoubleArray()): AudioSignal {
    return AudioSignal(
        samples = samples,
        sampleRate = Random.nextFloat()
    )
}
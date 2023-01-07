package toolkit.monkeyTest

import sound.input.samples.AudioSignal
import kotlin.random.Random

fun nextAudioSignal(): AudioSignal {
    return AudioSignal(
        samples = nextDoubleArray(),
        sampleRate = Random.nextFloat()
    )
}
package toolkit.monkeyTest

import sound.input.samples.AudioFrame
import kotlin.random.Random

fun nextAudioFrame(): AudioFrame {
    return AudioFrame(
        samples = nextDoubleArray(),
        sampleRate = Random.nextFloat()
    )
}
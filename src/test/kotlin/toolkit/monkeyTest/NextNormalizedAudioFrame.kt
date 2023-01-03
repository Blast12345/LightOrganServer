package toolkit.monkeyTest

import sound.input.samples.NormalizedAudioFrame
import kotlin.random.Random

fun nextNormalizedAudioFrame(): NormalizedAudioFrame {
    return NormalizedAudioFrame(
        samples = nextDoubleArray(),
        sampleRate = Random.nextFloat()
    )
}
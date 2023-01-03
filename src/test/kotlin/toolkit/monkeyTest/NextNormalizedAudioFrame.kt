package toolkit.monkeyTest

import sound.input.samples.NormalizedAudioFrame
import kotlin.random.Random

fun Random.nextNormalizedAudioFrame(): NormalizedAudioFrame {
    return NormalizedAudioFrame(
        samples = Random.nextDoubleArray(),
        sampleRate = Random.nextFloat()
    )
}
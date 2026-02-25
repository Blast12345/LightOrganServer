package toolkit.monkeyTest

import audio.samples.AudioFrame
import kotlin.random.Random

fun nextAudioFrame(
    sampleRate: Float = Random.nextFloat()
): AudioFrame {
    return AudioFrame(
        samples = nextFloatArray(),
        format = nextAudioFormat(sampleRate)
    )
}
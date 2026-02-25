package toolkit.monkeyTest

import audio.samples.AudioFormat
import kotlin.random.Random

fun nextAudioFormat(
    sampleRate: Float = Random.nextFloat(),
    channels: Int = nextPositiveInt()
): AudioFormat {
    return AudioFormat(
        sampleRate = sampleRate,
        bitDepth = nextPositiveInt(),
        channels = channels
    )
}

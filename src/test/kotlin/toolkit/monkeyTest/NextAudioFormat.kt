package toolkit.monkeyTest

import javax.sound.sampled.AudioFormat
import kotlin.random.Random

fun nextAudioFormat(): AudioFormat {
    return AudioFormat(
        Random.nextFloat(),
        nextPositiveInt(),
        nextPositiveInt(),
        Random.nextBoolean(),
        Random.nextBoolean()
    )
}
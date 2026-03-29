package toolkit.monkeyTest

import audio.samples.AudioStreamFrame
import kotlin.random.Random.Default.nextBoolean

fun nextAudioStreamFrame(): AudioStreamFrame {
    return AudioStreamFrame(
        audio = nextAudioFrame(),
        sequenceNumber = nextPositiveLong(),
        bufferWasFull = nextBoolean()
    )
}
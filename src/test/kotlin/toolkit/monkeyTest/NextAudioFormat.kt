package toolkit.monkeyTest

import wrappers.audioFormat.AudioFormatWrapper
import kotlin.random.Random

fun nextAudioFormatWrapper(): AudioFormatWrapper {
    return AudioFormatWrapper(
        sampleRate = Random.nextFloat(),
        numberOfChannels = Random.nextInt()
    )
}
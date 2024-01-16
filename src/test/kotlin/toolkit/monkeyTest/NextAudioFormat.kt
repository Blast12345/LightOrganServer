package toolkit.monkeyTest

import wrappers.audioFormat.AudioFormatWrapper
import kotlin.random.Random

fun nextAudioFormatWrapper(
    nyquistFrequency: Float = Random.nextFloat(),
    numberOfChannels: Int = Random.nextInt()
): AudioFormatWrapper {
    return AudioFormatWrapper(
        sampleRate = Random.nextFloat(),
        nyquistFrequency = nyquistFrequency,
        numberOfChannels = numberOfChannels
    )
}

package toolkit.monkeyTest

import sound.frequencyBins.FrequencyBin
import kotlin.random.Random

fun nextFrequencyBin(frequency: Float = Random.nextFloat()): FrequencyBin {
    return FrequencyBin(
        frequency = frequency,
        magnitude = Random.nextFloat()
    )
}
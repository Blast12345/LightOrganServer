package toolkit.monkeyTest

import sound.frequencyBins.FrequencyBin
import kotlin.random.Random

fun nextFrequencyBin(): FrequencyBin {
    return FrequencyBin(
        frequency = Random.nextFloat(),
        magnitude = Random.nextFloat()
    )
}
package toolkit.monkeyTest

import sound.frequencyBins.FrequencyBin
import kotlin.random.Random

fun Random.nextFrequencyBin(): FrequencyBin {
    return FrequencyBin(
        frequency = Random.nextDouble(),
        amplitude = Random.nextDouble()
    )
}
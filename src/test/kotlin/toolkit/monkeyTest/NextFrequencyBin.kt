package toolkit.monkeyTest

import sound.frequencyBins.FrequencyBin
import kotlin.random.Random

fun Random.nextFrequencyBin(): FrequencyBin {
    return FrequencyBin(
        frequency = Random.nextFloat(),
        amplitude = Random.nextDouble()
    )
}
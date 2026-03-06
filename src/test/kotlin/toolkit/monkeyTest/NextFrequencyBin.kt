package toolkit.monkeyTest

import bins.FrequencyBin
import kotlin.random.Random

fun nextFrequencyBin(
    frequency: Float = Random.nextFloat(),
    magnitude: Float = Random.nextFloat()
): FrequencyBin {
    return FrequencyBin(
        frequency = frequency,
        magnitude = magnitude
    )
}

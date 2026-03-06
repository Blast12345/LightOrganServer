package toolkit.monkeyTest

import bins.FrequencyBin
import bins.FrequencyBins
import kotlin.random.Random

fun nextFrequencyBins(
    length: Int = Random.nextInt(3, 10)
): FrequencyBins {
    val list: MutableList<FrequencyBin> = mutableListOf()

    repeat(length) {
        list.add(nextFrequencyBin())
    }

    return list.toList()
}

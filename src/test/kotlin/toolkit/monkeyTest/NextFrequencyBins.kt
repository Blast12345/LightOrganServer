package toolkit.monkeyTest

import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBins
import kotlin.random.Random

fun nextFrequencyBins(): FrequencyBins {
    val length = Random.nextInt() % 10
    var list: MutableList<FrequencyBin> = mutableListOf()

    for (i in 0 until length) {
        list.add(nextFrequencyBin())
    }

    return list.toList()
}
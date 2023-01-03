package toolkit.monkeyTest

import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBins
import kotlin.random.Random

fun Random.nextFrequencyBins(): FrequencyBins {
    val length = Random.nextInt() % 10
    var list: MutableList<FrequencyBin> = mutableListOf()

    for (i in 0..length) {
        list.add(Random.nextFrequencyBin())
    }

    return list.toList()
}
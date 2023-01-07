package toolkit.monkeyTest

import sound.frequencyBins.FrequencyBin
import sound.frequencyBins.FrequencyBinList
import kotlin.random.Random

fun nextFrequencyBins(): FrequencyBinList {
    val length = Random.nextInt() % 10
    var list: MutableList<FrequencyBin> = mutableListOf()

    for (i in 0 until length) {
        list.add(nextFrequencyBin())
    }

    return list.toList()
}
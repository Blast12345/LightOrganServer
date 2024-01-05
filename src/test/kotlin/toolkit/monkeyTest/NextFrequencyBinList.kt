package toolkit.monkeyTest

import sound.bins.frequencyBins.FrequencyBin
import sound.bins.frequencyBins.FrequencyBinList
import kotlin.random.Random

fun nextFrequencyBinList(
    length: Int = Random.nextInt(1, 10)
): FrequencyBinList {
    val list: MutableList<FrequencyBin> = mutableListOf()

    for (i in 0 until length) {
        list.add(nextFrequencyBin())
    }

    return list.toList()
}
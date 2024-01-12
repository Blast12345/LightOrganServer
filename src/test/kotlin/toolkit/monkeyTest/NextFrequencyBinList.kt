package toolkit.monkeyTest

import sound.bins.frequency.FrequencyBin
import sound.bins.frequency.FrequencyBinList
import kotlin.random.Random

fun nextFrequencyBinList(
    length: Int = Random.nextInt(1, 10)
): FrequencyBinList {
    val list: MutableList<FrequencyBin> = mutableListOf()

    repeat(length) {
        list.add(nextFrequencyBin())
    }

    return list.toList()
}

package toolkit.monkeyTest

import lightOrgan.sound.frequencyBins.FrequencyBin
import lightOrgan.sound.frequencyBins.FrequencyBinList
import kotlin.random.Random

fun nextFrequencyBins(length: Int = Random.nextInt() % 10): FrequencyBinList {
    var list: MutableList<FrequencyBin> = mutableListOf()

    for (i in 0 until length) {
        list.add(nextFrequencyBin())
    }

    return list.toList()
}
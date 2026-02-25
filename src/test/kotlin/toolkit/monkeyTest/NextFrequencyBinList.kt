package toolkit.monkeyTest

import dsp.bins.frequency.FrequencyBin
import dsp.bins.frequency.FrequencyBins
import kotlin.random.Random

fun nextFrequencyBins(
    length: Int = Random.nextInt(1, 10)
): FrequencyBins {
    val list: MutableList<FrequencyBin> = mutableListOf()

    repeat(length) {
        list.add(nextFrequencyBin())
    }

    return list.toList()
}

package toolkit.monkeyTest

import dsp.fft.FrequencyBin
import dsp.fft.FrequencyBins
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

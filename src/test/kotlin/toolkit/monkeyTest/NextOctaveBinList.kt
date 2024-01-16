package toolkit.monkeyTest

import sound.bins.octave.OctaveBin
import sound.bins.octave.OctaveBins
import kotlin.random.Random

fun nextOctaveBins(
    length: Int = Random.nextInt(1, 10)
): OctaveBins {
    val list: MutableList<OctaveBin> = mutableListOf()

    repeat(length) {
        list.add(nextOctaveBin())
    }

    return list.toList()
}

package toolkit.monkeyTest

import sound.bins.octave.OctaveBin
import sound.bins.octave.OctaveBinList
import kotlin.random.Random

fun nextOctaveBinList(
    length: Int = Random.nextInt(1, 10)
): OctaveBinList {
    val list: MutableList<OctaveBin> = mutableListOf()

    repeat(length) {
        list.add(nextOctaveBin())
    }

    return list.toList()
}

package toolkit.monkeyTest

import sound.octave.OctaveBin
import sound.octave.OctaveBinList
import kotlin.random.Random

fun nextOctaveBinList(
    length: Int = Random.nextInt(1, 10)
): OctaveBinList {
    val list: MutableList<OctaveBin> = mutableListOf()

    for (i in 0 until length) {
        list.add(nextOctaveBin())
    }

    return list.toList()
}
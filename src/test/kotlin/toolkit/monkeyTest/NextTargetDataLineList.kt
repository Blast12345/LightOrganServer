package toolkit.monkeyTest

import javax.sound.sampled.TargetDataLine

fun nextTargetDataLineList(length: Int): List<TargetDataLine> {
    val list: MutableList<TargetDataLine> = mutableListOf()

    for (i in 0 until length) {
        list.add(nextTargetDataLine())
    }

    return list.toList()
}

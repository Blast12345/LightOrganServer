package toolkit.monkeyTest

import javax.sound.sampled.TargetDataLine

fun nextTargetDataLineList(length: Int): List<TargetDataLine> {
    val list: MutableList<TargetDataLine> = mutableListOf()

    repeat(length) {
        list.add(nextTargetDataLine())
    }

    return list.toList()
}

package toolkit.monkeyTest

import javax.sound.sampled.TargetDataLine
import kotlin.random.Random

fun nextTargetDataLineList(): List<TargetDataLine> {
    val length = Random.nextInt() % 10
    var list: MutableList<TargetDataLine> = mutableListOf()

    for (i in 0 until length) {
        list.add(nextTargetDataLine())
    }

    return list.toList()
}
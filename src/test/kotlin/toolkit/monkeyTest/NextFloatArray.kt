package toolkit.monkeyTest

import kotlin.random.Random

fun nextFloatArray(length: Int = Random.nextInt(1024)): FloatArray {
    val list: MutableList<Float> = mutableListOf()

    repeat(length) {
        list.add(Random.nextFloat())
    }

    return list.toFloatArray()
}

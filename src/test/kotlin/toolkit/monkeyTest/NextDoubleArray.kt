package toolkit.monkeyTest

import kotlin.random.Random

fun nextDoubleArray(length: Int = Random.nextInt() % 10): DoubleArray {
    var list: MutableList<Double> = mutableListOf()

    for (i in 0 until length) {
        list.add(Random.nextDouble())
    }

    return list.toDoubleArray()
}
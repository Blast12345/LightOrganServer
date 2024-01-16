package toolkit.monkeyTest

import kotlin.random.Random

fun nextDoubleArray(length: Int = Random.nextInt() % 10): DoubleArray {
    val list: MutableList<Double> = mutableListOf()

    repeat(length) {
        list.add(Random.nextDouble())
    }

    return list.toDoubleArray()
}

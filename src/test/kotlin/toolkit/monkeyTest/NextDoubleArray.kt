package toolkit.monkeyTest

import kotlin.random.Random

fun Random.nextDoubleArray(): DoubleArray {
    val length = Random.nextInt() % 10
    var list: MutableList<Double> = mutableListOf()

    for (i in 0..length) {
        list.add(Random.nextDouble())
    }

    return list.toDoubleArray()
}
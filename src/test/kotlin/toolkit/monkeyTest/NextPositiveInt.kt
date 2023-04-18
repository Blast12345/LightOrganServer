package toolkit.monkeyTest

import kotlin.random.Random

fun nextPositiveInt(): Int {
    return Random.nextInt(0, 1024)
}
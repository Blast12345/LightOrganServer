package toolkit.monkeyTest

import kotlin.random.Random

fun nextPositiveLong(): Long {
    return Random.nextLong(0, 1024)
}
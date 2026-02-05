package toolkit.monkeyTest

import kotlin.random.Random

fun nextPositiveInt(max: Int = 1024): Int {
    return Random.nextInt(max)
}

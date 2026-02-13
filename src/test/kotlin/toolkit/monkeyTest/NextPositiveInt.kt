package toolkit.monkeyTest

import kotlin.random.Random

fun nextPositiveInt(min: Int = 1, max: Int = 1024): Int {
    return Random.nextInt(min, max)
}

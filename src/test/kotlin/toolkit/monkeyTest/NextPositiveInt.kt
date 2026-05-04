package toolkit.monkeyTest

import kotlin.random.Random

fun nextInt(min: Int = 1, max: Int = 1024): Int {
    require(min >= 0) { "min must be a positive number" }

    return Random.nextInt(min, max)
}

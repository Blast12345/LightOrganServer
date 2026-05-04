package toolkit.monkeyTest

import kotlin.random.Random

fun nextPositiveLong(min: Long = 1, max: Long = 1024): Long {
    require(min >= 0f) { "min must not be negative" }

    return Random.nextLong(min, max)
}

package toolkit.monkeyTest

import kotlin.random.Random

fun nextFloat(
    min: Float = 0F,
    max: Float = 1F
): Float {
    require(min < max)
    return Random.nextFloat() * (max - min) + min
}
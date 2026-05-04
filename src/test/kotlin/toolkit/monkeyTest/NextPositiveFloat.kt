package toolkit.monkeyTest

import kotlin.random.Random

fun nextPositiveFloat(min: Float = 1f, max: Float = 1024f): Float {
    require(min > 0f) { "min must be a positive number" }

    return Random
        .nextDouble(min.toDouble(), max.toDouble())
        .toFloat()
}
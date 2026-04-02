package toolkit.monkeyTest

import kotlin.random.Random

fun nextPositiveFloat(min: Int = 1, max: Int = 1024): Float {
    val base = nextPositiveInt(min, max)

    return base.toFloat() + Random.nextFloat()

}
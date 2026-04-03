package toolkit.monkeyTest

import kotlin.random.Random

// TODO: Technically this could be negative, so the name is a misnomer.
fun nextPositiveInt(min: Int = 1, max: Int = 1024): Int {
    return Random.nextInt(min, max)
}

package toolkit.monkeyTest

import math.normalization.UnitInterval
import kotlin.random.Random

fun nextUnitInterval(
    value: Double = Random.nextDouble()
): UnitInterval {
    return UnitInterval(value)
}
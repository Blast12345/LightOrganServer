package toolkit.monkeyTest

import config.children.HighPassFilter
import kotlin.random.Random

fun nextHighPassFilter(): HighPassFilter {
    return HighPassFilter(
        frequency = Random.nextFloat(),
        rollOffRange = Random.nextFloat()
    )
}
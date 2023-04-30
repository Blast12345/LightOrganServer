package toolkit.monkeyTest

import config.children.ColorWheel
import kotlin.random.Random

fun nextColorWheel(): ColorWheel {
    return ColorWheel(
        startingFrequency = Random.nextFloat(),
        endingFrequency = Random.nextFloat(),
        offset = Random.nextFloat()
    )
}

package toolkit.monkeyTest

import wrappers.color.Color
import kotlin.random.Random

fun nextColor(): Color {
    return Color(
        hue = Random.nextFloat(),
        saturation = Random.nextFloat(),
        brightness = Random.nextFloat()
    )
}

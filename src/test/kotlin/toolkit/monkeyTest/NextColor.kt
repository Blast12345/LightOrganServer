package toolkit.monkeyTest

import wrappers.color.Color
import kotlin.random.Random

fun nextColor(): Color {
    return Color(
        red = Random.nextInt(0, 256),
        green = Random.nextInt(0, 256),
        blue = Random.nextInt(0, 256)
    )
}

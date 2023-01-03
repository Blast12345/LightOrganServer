package toolkit.monkeyTest

import java.awt.Color
import kotlin.random.Random

fun nextColor(): Color {
    val red = Random.nextFloat()
    val green = Random.nextFloat()
    val blue = Random.nextFloat()
    return Color(red, green, blue)
}

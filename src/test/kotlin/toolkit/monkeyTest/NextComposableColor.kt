package toolkit.monkeyTest

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

fun nextComposeColor(): Color {
    val red = Random.nextFloat()
    val green = Random.nextFloat()
    val blue = Random.nextFloat()
    return Color(red, green, blue)
}

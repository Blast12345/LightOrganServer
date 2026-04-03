package toolkit.monkeyTest

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

fun nextColor(): Color {
    return Color.hsv(
        hue = Random.nextFloat() * 360f,
        saturation = Random.nextFloat(),
        value = Random.nextFloat()
    )
}

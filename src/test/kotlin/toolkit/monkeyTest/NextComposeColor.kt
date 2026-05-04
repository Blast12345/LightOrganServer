package toolkit.monkeyTest

import androidx.compose.ui.graphics.Color

fun nextComposeColor(): Color {
    return Color.hsv(
        nextPositiveFloat(max = 360),
        nextPositiveFloat(max = 1),
        nextPositiveFloat(max = 1)
    )
}
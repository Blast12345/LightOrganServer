package toolkit.monkeyTest

import androidx.compose.ui.graphics.Color

fun nextComposeColor(): Color {
    return Color.hsv(
        nextPositiveFloat(min = 0f, max = 360f),
        nextPositiveFloat(min = 0f, max = 1f),
        nextPositiveFloat(min = 0f, max = 1f)
    )
}
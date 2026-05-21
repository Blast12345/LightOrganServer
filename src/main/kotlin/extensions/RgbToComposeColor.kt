package extensions

import androidx.compose.ui.graphics.Color
import color.RgbColor

fun RgbColor.toComposeColor(): Color = Color(
    red = red.value.toFloat(),
    green = green.value.toFloat(),
    blue = blue.value.toFloat(),
    // TODO: Color space
)
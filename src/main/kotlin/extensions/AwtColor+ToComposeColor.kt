package extensions

import java.awt.Color

fun Color.toComposeColor(): androidx.compose.ui.graphics.Color {
    return androidx.compose.ui.graphics.Color.hsv(
        hue = getHue() * 360,
        saturation = getSaturation(),
        value = getBrightness()
    )
}
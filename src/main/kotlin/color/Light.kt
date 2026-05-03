package color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces

// TODO: Test me
// NOTE: This is similar to an RGB color, but this behaves more like the real world where the levels are not constrained
// NOTE: Light mixing is inherently linear. 500 lumens + 500 lumens = 1000 lumens
class Light(
    val red: Float = 0f,
    val green: Float = 0f,
    val blue: Float = 0f
) {

    private val normalizedColor: Color by lazy {
        val max = maxOf(red, green, blue)

        if (max == 0f) {
            return@lazy Color.Black
        } else {
            return@lazy Color(
                red / max,
                green / max,
                blue / max,
                colorSpace = ColorSpaces.LinearSrgb
            )
        }
    }


    val hue: Float by lazy {
        val r = normalizedColor.red
        val g = normalizedColor.green
        val b = normalizedColor.blue
        val max = maxOf(r, g, b)
        val min = minOf(r, g, b)
        val delta = max - min

        if (delta == 0f) return@lazy 0f

        val rawHue = when (max) {
            r -> 60f * (((g - b) / delta) % 6f)
            g -> 60f * (((b - r) / delta) + 2f)
            else -> 60f * (((r - g) / delta) + 4f)
        }

        if (rawHue < 0f) rawHue + 360f else rawHue
    }

    val saturation: Float by lazy {
        val r = normalizedColor.red
        val g = normalizedColor.green
        val b = normalizedColor.blue
        val max = maxOf(r, g, b)

        if (max == 0f) 0f else (max - minOf(r, g, b)) / max
    }


    operator fun plus(other: Light) = Light(
        red = red + other.red,
        green = green + other.green,
        blue = blue + other.blue
    )

    operator fun times(factor: Float) = Light(
        red = red * factor,
        green = green * factor,
        blue = blue * factor
    )

}


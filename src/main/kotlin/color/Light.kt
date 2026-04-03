package color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces

//import wrappers.color.Color

// TODO: Test me
// NOTE: This is similar to an RGB color, but this behaves more like the real world where the levels are not constrained
// i.e., r/g/b can all be greater than 255
class Light(
    val red: Float = 0f,
    val green: Float = 0f,
    val blue: Float = 0f
) {

    companion object {

        fun from(color: Color): Light {
            // Real light scales linearly
            val linearColor = color.convert(ColorSpaces.LinearSrgb) // TODO: Return RgbColor(r, g, b, linear)

            return Light(
                linearColor.component1(),
                linearColor.component2(),
                linearColor.component3()
            )
        }

    }

    val color: Color
        get() {
            val max = maxOf(red, green, blue)

            return Color(
                red / max,
                green / max,
                blue / max,
                colorSpace = ColorSpaces.LinearSrgb
            )
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


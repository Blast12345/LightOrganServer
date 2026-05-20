package color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import math.geometry.Angle
import math.normalization.UnitInterval

// ENHANCEMENT: Don't use compose color as the backing data due to compounding Float imprecision.
@JvmInline
value class StandardRgbColor private constructor(private val color: Color) : RgbColor {

    override val red: UnitInterval get() = UnitInterval(color.red.toDouble())
    override val green: UnitInterval get() = UnitInterval(color.green.toDouble())
    override val blue: UnitInterval get() = UnitInterval(color.blue.toDouble())
    override val alpha: UnitInterval get() = UnitInterval(color.alpha.toDouble())

    companion object {
        val Black = StandardRgbColor(Color.Black)

        fun fromRGB(
            red: UnitInterval,
            green: UnitInterval,
            blue: UnitInterval,
            alpha: UnitInterval = UnitInterval(1.0)
        ): StandardRgbColor {
            return StandardRgbColor(
                Color(
                    red.value.toFloat(),
                    green.value.toFloat(),
                    blue.value.toFloat(),
                    alpha.value.toFloat(),
                    ColorSpaces.Srgb
                )
            )
        }

        fun fromHSB(
            hue: Angle?,
            saturation: UnitInterval?,
            brightness: UnitInterval,
            alpha: UnitInterval = UnitInterval(1.0)
        ): StandardRgbColor {
            return StandardRgbColor(
                Color.hsv(
                    hue?.normalized?.degrees?.toFloat() ?: 0f,
                    saturation?.value?.toFloat() ?: 0f,
                    brightness.value.toFloat(),
                    alpha.value.toFloat(),
                    ColorSpaces.Srgb
                )
            )
        }
    }

    fun toComposeColor(): Color {
        return color
    }

}
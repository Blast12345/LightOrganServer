package color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import math.normalization.UnitInterval

@JvmInline
value class LinearRgbColor private constructor(private val color: Color) : RgbColor {

    override val red: UnitInterval get() = UnitInterval(color.red.toDouble())
    override val green: UnitInterval get() = UnitInterval(color.green.toDouble())
    override val blue: UnitInterval get() = UnitInterval(color.blue.toDouble())
    override val alpha: UnitInterval get() = UnitInterval(color.alpha.toDouble())

    fun toSrgb(): StandardRgbColor {
        return StandardRgbColor.fromRGB(
            red = UnitInterval(color.convert(ColorSpaces.Srgb).red.toDouble()),
            green = UnitInterval(color.convert(ColorSpaces.Srgb).green.toDouble()),
            blue = UnitInterval(color.convert(ColorSpaces.Srgb).blue.toDouble()),
            alpha = alpha
        )
    }

    companion object {
        fun fromRgb(
            red: UnitInterval,
            green: UnitInterval,
            blue: UnitInterval,
            alpha: UnitInterval = UnitInterval(1.0)
        ): LinearRgbColor {
            return LinearRgbColor(
                Color(
                    red.value.toFloat(),
                    green.value.toFloat(),
                    blue.value.toFloat(),
                    alpha.value.toFloat(),
                    ColorSpaces.LinearSrgb
                )
            )
        }
    }
}
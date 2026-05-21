package color

import math.geometry.Angle
import math.normalization.UnitInterval
import kotlin.math.abs

data class HsvColor<S : ColorSpace>(
    val hue: Angle,
    val saturation: UnitInterval,
    val brightness: UnitInterval
) {

    fun toRgb(): RgbColor<S> {
        val chroma = brightness.value * saturation.value
        val hueSegment = hue.degrees / 60.0
        val secondary = chroma * (1.0 - abs(hueSegment % 2.0 - 1.0))
        val match = brightness.value - chroma

        val (red, green, blue) = when {
            hueSegment < 1.0 -> Triple(chroma, secondary, 0.0)
            hueSegment < 2.0 -> Triple(secondary, chroma, 0.0)
            hueSegment < 3.0 -> Triple(0.0, chroma, secondary)
            hueSegment < 4.0 -> Triple(0.0, secondary, chroma)
            hueSegment < 5.0 -> Triple(secondary, 0.0, chroma)
            else -> Triple(chroma, 0.0, secondary)
        }

        return RgbColor(
            red = UnitInterval(red + match),
            green = UnitInterval(green + match),
            blue = UnitInterval(blue + match),
        )
    }

}
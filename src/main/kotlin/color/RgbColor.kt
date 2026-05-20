package color

import math.geometry.Angle
import math.normalization.UnitInterval

interface RgbColor {
    val red: UnitInterval
    val green: UnitInterval
    val blue: UnitInterval
    val alpha: UnitInterval

    val hue: Angle?
        get() {
            val max = maxOf(red.value, green.value, blue.value)
            val min = minOf(red.value, green.value, blue.value)
            val delta = max - min

            if (delta == 0.0) return null

            val degrees = when (max) {
                red.value -> 60.0 * (((green.value - blue.value) / delta) % 6.0)
                green.value -> 60.0 * (((blue.value - red.value) / delta) + 2.0)
                else -> 60.0 * (((red.value - green.value) / delta) + 4.0)
            }

            return Angle.fromDegrees(((degrees % 360.0) + 360.0) % 360.0)
        }

    val saturation: UnitInterval?
        get() {
            val max = maxOf(red.value, green.value, blue.value)
            if (max == 0.0) return null
            val min = minOf(red.value, green.value, blue.value)
            return UnitInterval((max - min) / max)
        }

    val brightness: UnitInterval
        get() = UnitInterval(maxOf(red.value, green.value, blue.value))
}
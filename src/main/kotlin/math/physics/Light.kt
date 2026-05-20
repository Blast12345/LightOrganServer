package math.physics

import color.Chromaticity
import math.geometry.Angle
import math.normalization.UnitInterval

typealias RadiantFlux = Double

// TODO: Test me
// This is a modeling of how light works in the real world (i.e. not based on human perception).
// It is measured in terms of radiant flux, of which the SI unit is watts.
// Light mixing is inherently linear. 500 watts + 500 watts = 1000 watts
// And like light in the real world, the brightness is not capped, so it can be any arbitrarily large size.
class Light(
    val red: RadiantFlux = 0.0,
    val green: RadiantFlux = 0.0,
    val blue: RadiantFlux = 0.0
) {

    val radiantFlux: RadiantFlux by lazy {
        red + green + blue
    }

    val chromaticity: Chromaticity
        get() {
            val max = maxOf(red, green, blue)
            if (max == 0.0) return Chromaticity(Angle.zero, UnitInterval.zero)

            val min = minOf(red, green, blue)
            val chroma = max - min

            val hue = if (chroma == 0.0) {
                Angle.zero
            } else {
                val degrees = when (max) {
                    red -> 60.0 * ((green - blue) / chroma).mod(6.0)
                    green -> 60.0 * ((blue - red) / chroma + 2.0)
                    else -> 60.0 * ((red - green) / chroma + 4.0)
                }
                Angle.fromDegrees(degrees)
            }

            val saturation = UnitInterval.clamped(chroma / max)

            return Chromaticity(hue, saturation)
        }

    operator fun plus(other: Light) = Light(
        red = red + other.red,
        green = green + other.green,
        blue = blue + other.blue
    )

    operator fun times(factor: Double) = Light(
        red = red * factor,
        green = green * factor,
        blue = blue * factor
    )

}
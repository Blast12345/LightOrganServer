package color

import math.normalization.UnitInterval
import kotlin.math.pow

data class RgbColor<S : RgbColorSpace>(
    val red: UnitInterval,
    val green: UnitInterval,
    val blue: UnitInterval,
)

// SRGB
typealias SrgbColor = RgbColor<Srgb>

object SrgbColors {
    val Black = SrgbColor(UnitInterval.zero, UnitInterval.zero, UnitInterval.zero)
    val White = SrgbColor(UnitInterval.one, UnitInterval.one, UnitInterval.one)
}

fun SrgbColor.toLinear(): LinearRgbColor = mapChannels { value ->
    if (value <= 0.04045) value / 12.92
    else ((value + 0.055) / 1.055).pow(2.4)
}


// Linear
typealias LinearRgbColor = RgbColor<LinearRgb>

object LinearRgbColors {
    val Black = LinearRgbColor(UnitInterval.zero, UnitInterval.zero, UnitInterval.zero)
    val White = LinearRgbColor(UnitInterval.one, UnitInterval.one, UnitInterval.one)
}

fun LinearRgbColor.toSrgb(): SrgbColor = mapChannels { value ->
    if (value <= 0.0031308) value * 12.92
    else 1.055 * value.pow(1.0 / 2.4) - 0.055
}

// Helpers
private fun <S : RgbColorSpace, T : RgbColorSpace> RgbColor<S>.mapChannels(
    transform: (Double) -> Double
): RgbColor<T> = RgbColor(
    red = UnitInterval(transform(red.value)),
    green = UnitInterval(transform(green.value)),
    blue = UnitInterval(transform(blue.value)),
)
package color

import math.normalization.UnitInterval

data class RgbColor<S : ColorSpace>(
    val red: UnitInterval,
    val green: UnitInterval,
    val blue: UnitInterval,
)

typealias SrgbColor = RgbColor<Srgb>
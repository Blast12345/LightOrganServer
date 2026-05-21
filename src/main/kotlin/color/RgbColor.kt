package color

import math.normalization.UnitInterval

data class RgbColor<S : RgbColorSpace>(
    val red: UnitInterval,
    val green: UnitInterval,
    val blue: UnitInterval,
)
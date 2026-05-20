package color

import math.geometry.Angle
import math.normalization.UnitInterval

data class Chromaticity(
    val hue: Angle,
    val saturation: UnitInterval
)
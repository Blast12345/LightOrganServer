package color

import math.geometry.Angle
import math.normalization.UnitInterval

// Reference: https://en.wikipedia.org/wiki/Chromaticity
data class Chromaticity(
    val hue: Angle,
    val saturation: UnitInterval
)
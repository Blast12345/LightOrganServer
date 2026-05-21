package color

import math.normalization.UnitInterval

typealias SrgbColor = RgbColor<Srgb>

object SrgbColors {
    val Black = SrgbColor(UnitInterval.zero, UnitInterval.zero, UnitInterval.zero)
}
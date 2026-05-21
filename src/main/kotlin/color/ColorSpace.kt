package color

import math.normalization.UnitInterval

sealed interface ColorSpace

data object Srgb : ColorSpace {
    val Black = RgbColor<Srgb>(UnitInterval.zero, UnitInterval.zero, UnitInterval.zero)
}
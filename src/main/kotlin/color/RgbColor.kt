package color

import math.normalization.UnitInterval

data class RgbColor(
    val red: UnitInterval,
    val green: UnitInterval,
    val blue: UnitInterval,
    // TODO: Color space
) {

    companion object {
        val Black = RgbColor(UnitInterval.zero, UnitInterval.zero, UnitInterval.zero)
    }

}
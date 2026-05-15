package lightOrgan.color

import math.normalization.UnitInterval

class SrgbColor(
    val red: UnitInterval,
    val green: UnitInterval,
    val blue: UnitInterval
) {

    companion object {
        val Black = SrgbColor(
            UnitInterval(0.0),
            UnitInterval(0.0),
            UnitInterval(0.0),
        )
    }

}
package toolkit.monkeyTest

import color.SrgbColor

fun nextSrgbColor(): SrgbColor {
    return SrgbColor(
        red = nextUnitInterval(),
        green = nextUnitInterval(),
        blue = nextUnitInterval(),
    )
}
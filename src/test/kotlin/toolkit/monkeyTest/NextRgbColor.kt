package toolkit.monkeyTest

import color.RgbColor

fun nextRgbColor(): RgbColor {
    return RgbColor(
        red = nextUnitInterval(),
        green = nextUnitInterval(),
        blue = nextUnitInterval(),
    )
}
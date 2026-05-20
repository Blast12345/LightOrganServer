package toolkit.monkeyTest

import color.StandardRgbColor

fun nextStandardRgbColor(): StandardRgbColor {
    return StandardRgbColor.fromRGB(
        red = nextUnitInterval(),
        green = nextUnitInterval(),
        blue = nextUnitInterval(),
    )
}
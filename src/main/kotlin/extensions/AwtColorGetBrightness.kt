package extensions

import java.awt.Color

fun Color.getBrightness(): Float {
    return Color.RGBtoHSB(red, green, blue, null)[2]
}

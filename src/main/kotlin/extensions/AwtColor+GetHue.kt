package extensions

import java.awt.Color

fun Color.getHue(): Float {
    return Color.RGBtoHSB(red, green, blue, null)[0]
}
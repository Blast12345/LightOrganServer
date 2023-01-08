package toolkit.color

import java.awt.Color

fun Color.getSaturation(): Float {
    return Color.RGBtoHSB(red, green, blue, null)[1]
}
package extensions

fun java.awt.Color.getBrightness(): Float {
    return java.awt.Color.RGBtoHSB(red, green, blue, null)[2]
}
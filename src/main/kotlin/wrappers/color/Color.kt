package wrappers.color

import extensions.getBrightness
import extensions.getHue
import extensions.getSaturation

class Color {

    val hue: Float
    val saturation: Float
    val brightness: Float
    val red: Int
    val green: Int
    val blue: Int

    constructor(
        hue: Float,
        saturation: Float,
        brightness: Float
    ) {
        val javaColor = java.awt.Color.getHSBColor(hue, saturation, brightness)
        this.hue = javaColor.getHue()
        this.saturation = javaColor.getSaturation()
        this.brightness = javaColor.getBrightness()
        this.red = javaColor.red
        this.green = javaColor.green
        this.blue = javaColor.blue
    }

    constructor(
        red: Int,
        green: Int,
        blue: Int
    ) {
        val javaColor = java.awt.Color(red, green, blue)
        this.hue = javaColor.getHue()
        this.saturation = javaColor.getSaturation()
        this.brightness = javaColor.getBrightness()
        this.red = javaColor.red
        this.green = javaColor.green
        this.blue = javaColor.blue
    }

    // Generated Code
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Color

        if (hue != other.hue) return false
        if (saturation != other.saturation) return false
        if (brightness != other.brightness) return false
        if (red != other.red) return false
        if (green != other.green) return false
        if (blue != other.blue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hue.hashCode()
        result = 31 * result + saturation.hashCode()
        result = 31 * result + brightness.hashCode()
        result = 31 * result + red
        result = 31 * result + green
        result = 31 * result + blue
        return result
    }

}

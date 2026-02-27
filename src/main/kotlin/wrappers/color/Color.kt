package wrappers.color

import extensions.getBrightness
import extensions.getHue
import extensions.getSaturation
import androidx.compose.ui.graphics.Color as ComposeColor
import java.awt.Color as JavaColor

// TODO: Refactor
class Color {

    val hue: Float
    val saturation: Float
    val brightness: Float
    val red: Int
    val green: Int
    val blue: Int

    companion object {
        val black = Color(0F, 0F, 0F)
    }

    constructor(
        hue: Float,
        saturation: Float,
        brightness: Float
    ) {
        val javaColor = JavaColor.getHSBColor(hue, saturation, brightness)
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
        val javaColor = JavaColor(red, green, blue)
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

    fun toComposeColor(): ComposeColor {
        return ComposeColor.hsv(
            hue = hue * 360,
            saturation = saturation,
            value = brightness
        )
    }

}

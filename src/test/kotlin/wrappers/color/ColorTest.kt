package wrappers.color

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ColorTest {

    // NOTE: I derived from an online HSV to RGB tool
    private val hue = 30F / 360F
    private val saturation = 0.5F
    private val brightness = 0.75F
    private val red = 191
    private val green = 143
    private val blue = 96

    @Test
    fun `color can be created given hue, saturation, and brightness`() {
        val color = Color(hue, saturation, brightness)

        assertEquals(hue, color.hue, 0.01F)
        assertEquals(saturation, color.saturation, 0.01F)
        assertEquals(brightness, color.brightness, 0.01F)
        assertEquals(red, color.red)
        assertEquals(green, color.green)
        assertEquals(blue, color.blue)
    }

    @Test
    fun `color can be created given red, green, and blue`() {
        val color = Color(red, green, blue)

        assertEquals(hue, color.hue, 0.01F)
        assertEquals(saturation, color.saturation, 0.01F)
        assertEquals(brightness, color.brightness, 0.01F)
        assertEquals(red, color.red)
        assertEquals(green, color.green)
        assertEquals(blue, color.blue)
    }

}
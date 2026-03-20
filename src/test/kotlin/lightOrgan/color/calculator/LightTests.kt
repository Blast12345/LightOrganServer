package lightOrgan.color.calculator

import color.Light
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

class LightTests {

    @Test
    fun `the levels are zero by default`() {
        val light = Light()

        assertEquals(0, light.red)
        assertEquals(0, light.green)
        assertEquals(0, light.blue)
    }

    // Brightness
    @Test
    fun `brightness is the combined output of all channels`() {
        val sut = Light(1, 2, 3)

        assertEquals(6, sut.brightness)
    }

    // Hue
    @Test
    fun `red light has a hue of zero`() {
        val redLight = Light(1, 0, 0)

        assertEquals(0.00f, redLight.hue!!, 0.01f)
    }

    @Test
    fun `green light has a hue of one third`() {
        val greenLight = Light(0, 1, 0)

        assertEquals(0.33f, greenLight.hue!!, 0.01f)
    }

    @Test
    fun `blue light has a hue of two thirds`() {
        val blueLight = Light(0, 0, 1)

        assertEquals(0.66f, blueLight.hue!!, 0.01f)
    }

    @Test
    fun `hue is undefined when there is no light`() {
        val noLight = Light(0, 0, 0)

        assertNull(noLight.hue)
    }

    @Test
    fun `hue is undefined when the light is white`() {
        val whiteLight = Light(1, 1, 1)

        assertNull(whiteLight.hue)
    }

    // Saturation
    @Test
    fun `pure red light has full saturation`() {
        val redLight = Light(1, 0, 0)

        assertEquals(1.00f, redLight.saturation!!, 0.01f)
    }

    @Test
    fun `desaturated red light has partial saturation`() {
        val light = Light(2, 1, 1)

        assertEquals(0.50f, light.saturation!!, 0.01f)
    }

    @Test
    fun `white light has no saturation`() {
        val whiteLight = Light(1, 1, 1)

        assertEquals(0.00f, whiteLight.saturation!!, 0.01f)
    }

    @Test
    fun `saturation is undefined when there is no light`() {
        val noLight = Light(0, 0, 0)

        assertNull(noLight.saturation)
    }

    // Combining
    @Test
    fun `combine two lights`() {
        val red = Light(1, 0, 0)
        val green = Light(0, 1, 0)

        val combined = red + green

        assertEquals(1, combined.red)
        assertEquals(1, combined.green)
        assertEquals(0, combined.blue)
    }

}
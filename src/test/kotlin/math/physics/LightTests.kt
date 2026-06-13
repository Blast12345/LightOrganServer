package math.physics

import color.Chromaticity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

class LightTests {

    private val red = Light(1.0, 0.0, 0.0)
    private val green = Light(0.0, 1.0, 0.0)
    private val blue = Light(0.0, 0.0, 1.0)
    private val yellow = Light(1.0, 1.0, 0.0)
    private val cyan = Light(0.0, 1.0, 1.0)
    private val white = Light(1.0, 1.0, 1.0)
    private val black = Light(0.0, 0.0, 0.0)

    // Init
    @Test
    fun `the levels are zero by default`() {
        val light = Light()

        assertEquals(0.0, light.red)
        assertEquals(0.0, light.green)
        assertEquals(0.0, light.blue)
    }

    // Intensity
    @Test
    fun `get the overall intensity of the light`() {
        val sut = Light(1.0, 2.0, 3.0)

        assertEquals(6.0, sut.radiantFlux, 0.001)
    }

    // Chromaticity - the color of light, independent of brightness
    @Test
    fun `chromaticity of red light`() {
        val chromaticity = red.chromaticity as Chromaticity.Chromatic

        assertEquals(0.0, chromaticity.hue.degrees, 0.01)
        assertEquals(1.0, chromaticity.saturation.value, 0.01)
    }

    @Test
    fun `chromaticity of green light`() {
        val chromaticity = green.chromaticity as Chromaticity.Chromatic

        assertEquals(120.0, chromaticity.hue.degrees, 0.01)
        assertEquals(1.0, chromaticity.saturation.value, 0.01)
    }

    @Test
    fun `chromaticity of blue light`() {
        val chromaticity = blue.chromaticity as Chromaticity.Chromatic

        assertEquals(240.0, chromaticity.hue.degrees, 0.01)
        assertEquals(1.0, chromaticity.saturation.value, 0.01)
    }

    @Test
    fun `chromaticity of desaturated red light`() {
        val light = Light(2.0, 1.0, 1.0)

        val chromaticity = light.chromaticity as Chromaticity.Chromatic

        assertEquals(0.0, chromaticity.hue.degrees, 0.01)
        assertEquals(0.5, chromaticity.saturation.value, 0.01)
    }

    @Test
    fun `white light is achromatic`() {
        assertEquals(Chromaticity.Achromatic, white.chromaticity)
    }

    @Test
    fun `chromaticity is null if there is no light`() {
        assertNull(black.chromaticity)
    }

    @Test
    fun `chromaticity is independent of brightness`() {
        val dim = Light(1.0, 0.0, 0.0)
        val bright = Light(5.0, 0.0, 0.0)

        assertEquals(dim.chromaticity, bright.chromaticity)
    }

    // Summation
    @Test
    fun `combine two lights`() {
        val combined = red + green

        assertEquals(1.0, combined.red)
        assertEquals(1.0, combined.green)
        assertEquals(0.0, combined.blue)
    }

    @Test
    fun `chromaticity of red and yellow mixes to orange`() {
        val combined = red + yellow

        val chromaticity = combined.chromaticity as Chromaticity.Chromatic
        assertEquals(30.0, chromaticity.hue.degrees, 0.5)
    }

    @Test
    fun `chromaticity of red and cyan mixes to white`() {
        val combined = red + cyan

        assertEquals(Chromaticity.Achromatic, combined.chromaticity)
    }

    // Multiplication
    @Test
    fun `scaling a light multiplies each channel`() {
        val light = Light(1.0, 2.0, 3.0)

        val actual = light.times(3.0)

        assertEquals(3.0, actual.red)
        assertEquals(6.0, actual.green)
        assertEquals(9.0, actual.blue)
    }

}
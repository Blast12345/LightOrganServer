package math.physics

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

class LightTests {

    // Init
    @Test
    fun `the levels are zero by default`() {
        val light = Light()

        Assertions.assertEquals(0.0, light.red)
        Assertions.assertEquals(0.0, light.green)
        Assertions.assertEquals(0.0, light.blue)
    }

    // Operators
    @Test
    fun `combine two lights`() {
        val red = Light(1.0, 0.0, 0.0)
        val green = Light(0.0, 2.0, 0.0)

        val combined = red + green

        Assertions.assertEquals(1.0, combined.red)
        Assertions.assertEquals(2.0, combined.green)
        Assertions.assertEquals(0.0, combined.blue)
    }

    // Intensity
    @Test
    fun `get the overall intensity of the light`() {
        val sut = Light(1.0, 2.0, 3.0)

        Assertions.assertEquals(6.0, sut.radiantFlux, 0.001)
    }

    // Chromaticity - the color of light, independent of brightness
    @Test
    fun `chromaticity of red light`() {
        val light = Light(1.0, 0.0, 0.0)

        val chromaticity = light.chromaticity!!

        Assertions.assertEquals(0.0, chromaticity.hue!!.turns, 0.01)
        Assertions.assertEquals(1.0, chromaticity.saturation.value, 0.01)
    }

    @Test
    fun `chromaticity of green light`() {
        val light = Light(0.0, 1.0, 0.0)

        val chromaticity = light.chromaticity!!

        Assertions.assertEquals(0.33, chromaticity.hue!!.turns, 0.01)
        Assertions.assertEquals(1.0, chromaticity.saturation.value, 0.01)
    }

    @Test
    fun `chromaticity of blue light`() {
        val light = Light(0.0, 0.0, 1.0)

        val chromaticity = light.chromaticity!!

        Assertions.assertEquals(0.66, chromaticity.hue!!.turns, 0.01)
        Assertions.assertEquals(1.0, chromaticity.saturation.value, 0.01)
    }

    @Test
    fun `chromaticity of desaturated red light`() {
        val light = Light(2.0, 1.0, 1.0)

        val chromaticity = light.chromaticity!!

        Assertions.assertEquals(0.0, chromaticity.hue!!.turns, 0.01)
        Assertions.assertEquals(0.5, chromaticity.saturation.value, 0.01)
    }

    @Test
    fun `chromaticity of white light`() {
        val light = Light(1.0, 1.0, 1.0)

        val chromaticity = light.chromaticity!!

        assertNull(chromaticity.hue)
        Assertions.assertEquals(0.0, chromaticity.saturation.value, 0.01)
    }

    @Test
    fun `chromaticity is null if there is no light`() {
        val light = Light(0.0, 0.0, 0.0)

        val chromaticity = light.chromaticity

        assertNull(chromaticity)
    }

    @Test
    fun `chromaticity is independent of brightness`() {
        val dim = Light(1.0, 0.0, 0.0)
        val bright = Light(5.0, 0.0, 0.0)

        Assertions.assertEquals(dim.chromaticity, bright.chromaticity)
    }

}
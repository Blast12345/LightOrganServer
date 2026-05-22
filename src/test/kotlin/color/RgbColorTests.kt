package color

import math.normalization.UnitInterval
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

// There isn't a convenient reference table for color space conversions, so I'm leveraging other tools to generate validation data.
// Reference: https://silvesthu.github.io/tools/srgb.html
class RgbColorTests {

    @Nested
    inner class StandardRGBToLinear {

        @Test
        fun `low value uses linear segment`() {
            val srgb = StandardRgbColor(
                red = UnitInterval(0.02),
                green = UnitInterval(0.02),
                blue = UnitInterval(0.02),
            )

            val linear = srgb.toLinear()

            assertColorEquals(
                expected = LinearRgbColor(
                    red = UnitInterval(0.001547988),
                    green = UnitInterval(0.001547988),
                    blue = UnitInterval(0.001547988),
                ),
                actual = linear,
            )
        }

        @Test
        fun `mid value uses gamma curve`() {
            val srgb = StandardRgbColor(
                red = UnitInterval(0.5),
                green = UnitInterval(0.5),
                blue = UnitInterval(0.5),
            )

            val linear = srgb.toLinear()

            assertColorEquals(
                expected = LinearRgbColor(
                    red = UnitInterval(0.214041140),
                    green = UnitInterval(0.214041140),
                    blue = UnitInterval(0.214041140),
                ),
                actual = linear,
            )
        }

        @Test
        fun `converting to linear and back yields the original color`() {
            val original = StandardRgbColor(
                red = UnitInterval(0.3),
                green = UnitInterval(0.6),
                blue = UnitInterval(0.9),
            )

            val roundTripped = original.toLinear().toSrgb()

            assertColorEquals(expected = original, actual = roundTripped)
        }
    }

    @Nested
    inner class LinearToStandardRGB {

        @Test
        fun `low value uses linear segment`() {
            val linear = LinearRgbColor(
                red = UnitInterval(0.002),
                green = UnitInterval(0.002),
                blue = UnitInterval(0.002),
            )

            val srgb = linear.toSrgb()

            assertColorEquals(
                expected = StandardRgbColor(
                    red = UnitInterval(0.025840),
                    green = UnitInterval(0.025840),
                    blue = UnitInterval(0.025840),
                ),
                actual = srgb,
            )
        }

        @Test
        fun `mid value uses gamma curve`() {
            val linear = LinearRgbColor(
                red = UnitInterval(0.25),
                green = UnitInterval(0.25),
                blue = UnitInterval(0.25),
            )

            val srgb = linear.toSrgb()

            assertColorEquals(
                expected = StandardRgbColor(
                    red = UnitInterval(0.537098729),
                    green = UnitInterval(0.537098729),
                    blue = UnitInterval(0.537098729),
                ),
                actual = srgb,
            )
        }

        @Test
        fun `converting to srgb and back yields the original color`() {
            val original = LinearRgbColor(
                red = UnitInterval(0.1),
                green = UnitInterval(0.4),
                blue = UnitInterval(0.7),
            )

            val roundTripped = original.toSrgb().toLinear()

            assertColorEquals(expected = original, actual = roundTripped)
        }
    }

    // Helpers
    private fun <S : RgbColorSpace> assertColorEquals(
        expected: RgbColor<S>,
        actual: RgbColor<S>,
        tolerance: Double = 1e-6,
    ) {
        assertEquals(expected.red.value, actual.red.value, tolerance, "red")
        assertEquals(expected.green.value, actual.green.value, tolerance, "green")
        assertEquals(expected.blue.value, actual.blue.value, tolerance, "blue")
    }

}
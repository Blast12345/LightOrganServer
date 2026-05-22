package color

import math.geometry.Angle
import math.normalization.UnitInterval
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HsbColorTests {


    // To RGB
    @Test
    fun `given the color is white, convert to RGB`() {
        val color = HsbColor<Srgb>(Angle.zero, UnitInterval.zero, UnitInterval.one)

        val actual = color.toRgb()

        assertEquals(1.0, actual.red.value, 0.001)
        assertEquals(1.0, actual.green.value, 0.001)
        assertEquals(1.0, actual.blue.value, 0.001)
    }

    @Test
    fun `given the color is black, convert to RGB`() {
        val color = HsbColor<Srgb>(Angle.zero, UnitInterval.zero, UnitInterval.zero)

        val actual = color.toRgb()

        assertEquals(0.0, actual.red.value, 0.001)
        assertEquals(0.0, actual.green.value, 0.001)
        assertEquals(0.0, actual.blue.value, 0.001)
    }

    @Test
    fun `given the color is purple, convert to RGB`() {
        val color = HsbColor<Srgb>(
            Angle.fromDegrees(30.0),
            UnitInterval(0.8),
            UnitInterval(0.9),
        )

        val actual = color.toRgb()

        assertEquals(0.9, actual.red.value, 0.001)
        assertEquals(0.54, actual.green.value, 0.001)
        assertEquals(0.18, actual.blue.value, 0.001)
    }

}
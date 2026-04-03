package math.geometry

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AngleTests {

    // Degrees -> X
    @Test
    fun `convert from degrees to radians`() {
        val angle = Angle.fromDegrees(180.0)
        assertEquals(Math.PI, angle.radians, 0.001)
    }

    @Test
    fun `convert from degrees to turns`() {
        val angle = Angle.fromDegrees(90.0)
        assertEquals(0.25, angle.turns, 0.001)
    }

    // Radians -> X
    @Test
    fun `convert from radians to degrees`() {
        val angle = Angle.fromRadians(Math.PI)
        assertEquals(180.0, angle.degrees, 0.001)
    }

    @Test
    fun `convert from radians to turns`() {
        val angle = Angle.fromRadians(2 * Math.PI)
        assertEquals(1.0, angle.turns, 0.001)
    }

    // Turns -> X
    @Test
    fun `convert from turns to radians`() {
        val angle = Angle.fromTurns(0.5)
        assertEquals(Math.PI, angle.radians, 0.001)
    }

    @Test
    fun `convert from turns to degrees`() {
        val angle = Angle.fromTurns(1.5)

        assertEquals(540.0, angle.degrees, 0.001)
    }

}
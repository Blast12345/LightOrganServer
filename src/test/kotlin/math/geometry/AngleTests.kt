package math.geometry

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AngleTests {

    private val precision: Double = 1e-10

    // radians
    @Test
    fun `given positive radians, create angle`() {
        val angle = Angle.fromRadians(Math.PI)

        assertEquals(Math.PI, angle.radians, precision)
        assertEquals(180.0, angle.degrees, precision)
        assertEquals(0.5, angle.turns, precision)
    }

    @Test
    fun `given negative radians, create angle`() {
        val angle = Angle.fromRadians(-Math.PI / 2)

        assertEquals(-Math.PI / 2, angle.radians, precision)
        assertEquals(-90.0, angle.degrees, precision)
        assertEquals(-0.25, angle.turns, precision)
    }

    @Test
    fun `given radians beyond one rotation, create angle`() {
        val angle = Angle.fromRadians(4 * Math.PI)

        assertEquals(4 * Math.PI, angle.radians, precision)
        assertEquals(720.0, angle.degrees, precision)
        assertEquals(2.0, angle.turns, precision)
    }

    @Test
    fun `given radians as float, get angle`() {
        val angle = Angle.fromRadians(1.5f)

        assertEquals(1.5, angle.radians, precision)
    }

    // degrees
    @Test
    fun `given positive degrees, create angle`() {
        val angle = Angle.fromDegrees(180.0)

        assertEquals(Math.PI, angle.radians, precision)
        assertEquals(180.0, angle.degrees, precision)
        assertEquals(0.5, angle.turns, precision)
    }

    @Test
    fun `given negative degrees, create angle`() {
        val angle = Angle.fromDegrees(-90.0)

        assertEquals(-Math.PI / 2, angle.radians, precision)
        assertEquals(-90.0, angle.degrees, precision)
        assertEquals(-0.25, angle.turns, precision)
    }

    @Test
    fun `given degrees beyond one rotation, create angle`() {
        val angle = Angle.fromDegrees(720.0)

        assertEquals(4 * Math.PI, angle.radians, precision)
        assertEquals(720.0, angle.degrees, precision)
        assertEquals(2.0, angle.turns, precision)
    }

    // turns
    @Test
    fun `given positive turns, create angle`() {
        val angle = Angle.fromTurns(1.0)

        assertEquals(2 * Math.PI, angle.radians, precision)
        assertEquals(360.0, angle.degrees, precision)
        assertEquals(1.0, angle.turns, precision)
    }

    @Test
    fun `given negative turns, create angle`() {
        val angle = Angle.fromTurns(-0.5)

        assertEquals(-Math.PI, angle.radians, precision)
        assertEquals(-180.0, angle.degrees, precision)
        assertEquals(-0.5, angle.turns, precision)
    }

    @Test
    fun `given turns beyond one rotation, create angle`() {
        val angle = Angle.fromTurns(3.5)

        assertEquals(7 * Math.PI, angle.radians, precision)
        assertEquals(1260.0, angle.degrees, precision)
        assertEquals(3.5, angle.turns, precision)
    }

}
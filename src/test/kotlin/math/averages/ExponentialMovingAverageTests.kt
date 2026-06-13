package math.averages

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import toolkit.monkeyTest.nextDuration
import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TestTimeSource

class ExponentialMovingAverageTest {

    private val timeSource = TestTimeSource()
    private val halfLife: Duration = nextDuration()

    private fun averageOfDoubles(halfLife: Duration = this.halfLife) = ExponentialMovingAverage<Double>(
        halfLife = halfLife,
        scale = { value, factor -> value * factor },
        add = { first, second -> first + second },
        timeSource = timeSource
    )

    // Init
    @Test
    fun `when half life is zero, throw`() {
        assertThrows<IllegalArgumentException> {
            averageOfDoubles(0.milliseconds)
        }
    }

    @Test
    fun `when half life is negative, throw`() {
        assertThrows<IllegalArgumentException> {
            averageOfDoubles(-halfLife)
        }
    }

    @Test
    fun `the first update returns the original value`() {
        val sut = averageOfDoubles()

        val actual = sut.update(7.0)

        assertEquals(7.0, actual)
    }

    // Average accessor
    @Test
    fun `observe the average without updating the average`() {
        val sut = averageOfDoubles()

        assertNull(sut.average)

        val update1 = sut.update(1.0)
        assertEquals(update1, sut.average!!, 1e-6)

        timeSource += halfLife
        val update2 = sut.update(4.0)
        assertEquals(update2, sut.average!!, 1e-6)
    }

    // Rise
    @Test
    fun `rises along the exponential curve at fractional half-lives`() {
        val sut = averageOfDoubles()
        sut.update(0.0)

        timeSource += (halfLife / 2)
        val actual = sut.update(1.0)

        assertEquals(1.0 - 2.0.pow(-0.5), actual, 1e-9)
    }

    @Test
    fun `rises halfway to a new value after one half-life`() {
        val sut = averageOfDoubles()
        sut.update(0.0)

        timeSource += halfLife
        val actual = sut.update(1.0)

        assertEquals(0.5, actual, 1e-9)
    }

    @Test
    fun `rises along the exponential curve beyond one half-life`() {
        val sut = averageOfDoubles()
        sut.update(0.0)

        timeSource += (halfLife * 2)
        val actual = sut.update(1.0)

        assertEquals(0.75, actual, 1e-9)
    }

    // Fall
    @Test
    fun `falls along the exponential curve at fractional half-lives`() {
        val sut = averageOfDoubles()
        sut.update(1.0)

        timeSource += (halfLife / 2)
        val actual = sut.update(0.0)

        assertEquals(2.0.pow(-0.5), actual, 1e-9)
    }

    @Test
    fun `falls halfway to a new value after one half-life`() {
        val sut = averageOfDoubles()
        sut.update(1.0)

        timeSource += halfLife
        val actual = sut.update(0.0)

        assertEquals(0.5, actual, 1e-9)
    }

    @Test
    fun `falls along the exponential curve beyond one half-life`() {
        val sut = averageOfDoubles()
        sut.update(1.0)

        timeSource += (halfLife * 2)
        val actual = sut.update(0.0)

        assertEquals(0.25, actual, 1e-9)
    }

    // Convergence
    @Test
    fun `converges to a sustained new value`() {
        val sut = averageOfDoubles()
        sut.update(1.0)

        repeat(20) {
            timeSource += halfLife
            sut.update(0.5)
        }

        assertEquals(0.5, sut.average!!, 1e-6)
    }

    // Rate independence
    @Test
    fun `constant input remains constant regardless of update rate`() {
        val sut = averageOfDoubles()
        sut.update(1.0)

        timeSource += nextDuration()
        val observation1 = sut.update(1.0)

        timeSource += nextDuration()
        val observation2 = sut.update(1.0)

        timeSource += nextDuration()
        val observation3 = sut.update(1.0)

        assertEquals(1.0, observation1, 1e-9)
        assertEquals(1.0, observation2, 1e-9)
        assertEquals(1.0, observation3, 1e-9)
    }

    @Test
    fun `immediate repeated observation does not move the average`() {
        val sut = averageOfDoubles()
        sut.update(1.0)

        val actual = sut.update(5.0)

        // Zero elapsed time means the new observation has zero weight:
        // a sample's weight reflects the span of time it represents, and this one represents none.
        // This is the formula's own limit at zero, not a special case.
        assertEquals(1.0, actual, 1e-9)
    }

    // Genericness
    @Test
    fun `averages multi-component values per component`() {
        data class Vector(val x: Double, val y: Double)

        val sut = ExponentialMovingAverage<Vector>(
            halfLife = halfLife,
            scale = { vector, factor -> Vector(vector.x * factor, vector.y * factor) },
            add = { first, second -> Vector(first.x + second.x, first.y + second.y) },
            timeSource = timeSource
        )
        sut.update(Vector(1.0, 0.0))

        timeSource += halfLife
        val actual = sut.update(Vector(0.0, 1.0))

        assertEquals(0.5, actual.x, 1e-9)
        assertEquals(0.5, actual.y, 1e-9)
    }

}
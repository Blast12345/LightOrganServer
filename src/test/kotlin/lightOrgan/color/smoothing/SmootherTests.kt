package lightOrgan.color.smoothing

import lightOrgan.color.Smoothers
import math.physics.Light
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextDuration
import kotlin.time.TestTimeSource

class SmootherTests {

    private val timeSource = TestTimeSource()
    private val halfLife = nextDuration()
    private val attackHalfLife = nextDuration()
    private val releaseHalfLife = nextDuration()

    @Test
    fun `smooth light channels by the half-life`() {
        val sut = Smoothers.lightExponentialMovingAverage(halfLife, timeSource)
        sut.smooth(Light(1.0, 0.0, 0.5))

        timeSource += halfLife
        val actual = sut.smooth(Light(0.0, 1.0, 0.5))

        assertEquals(0.5, actual.red, 1e-9)
        assertEquals(0.5, actual.green, 1e-9)
        assertEquals(0.5, actual.blue, 1e-9)
    }

    @Test
    fun `smooth a scalar with the attack and release half-lives`() {
        val sut = Smoothers.scalarEnvelope(attackHalfLife, releaseHalfLife, timeSource)
        sut.smooth(0.0)

        timeSource += attackHalfLife
        val risen = sut.smooth(1.0)
        assertEquals(0.5, risen, 1e-9)

        timeSource += releaseHalfLife
        val fallen = sut.smooth(0.0)
        assertEquals(0.25, fallen, 1e-9)
    }

}
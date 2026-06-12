package lightOrgan.color.smoothing

import math.physics.Light
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TestTimeSource

class SmootherTests {

    private val timeSource = TestTimeSource()
    private val halfLife = 100.milliseconds

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

}
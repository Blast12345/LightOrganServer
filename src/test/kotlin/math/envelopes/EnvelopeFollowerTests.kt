package math.envelopes

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import toolkit.monkeyTest.nextDuration
import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TestTimeSource

class EnvelopeFollowerTests {

    private val attackHalfLife: Duration = nextDuration(min = 1.milliseconds)
    private val releaseHalfLife: Duration = nextDuration(min = 1.milliseconds)
    private val timeSource = TestTimeSource()

    private fun createSUT(
        attackHalfLife: Duration = this.attackHalfLife,
        releaseHalfLife: Duration = this.releaseHalfLife
    ): EnvelopeFollower {
        return EnvelopeFollower(
            attackHalfLife = attackHalfLife,
            releaseHalfLife = releaseHalfLife,
            timeSource = timeSource
        )
    }

    // Init
    @Test
    fun `when attack half life is negative, throw`() {
        assertThrows<IllegalArgumentException> {
            createSUT(attackHalfLife = -attackHalfLife)
        }
    }

    @Test
    fun `when release half life is negative, throw`() {
        assertThrows<IllegalArgumentException> {
            createSUT(releaseHalfLife = -releaseHalfLife)
        }
    }

    @Test
    fun `the first update returns the original value`() {
        val sut = createSUT()

        val actual = sut.update(7.0)

        assertEquals(7.0, actual)
    }

    // Envelope accessor
    @Test
    fun `the envelope is null before any update`() {
        val sut = createSUT()

        assertNull(sut.envelope)
    }

    @Test
    fun `the envelope matches the latest update`() {
        val sut = createSUT()

        val update1 = sut.update(1.0)
        assertEquals(update1, sut.envelope!!, 1e-9)

        timeSource += nextDuration()
        val update2 = sut.update(0.5)
        assertEquals(update2, sut.envelope!!, 1e-9)
    }

    @Test
    fun `time passing does not change the envelope between updates`() {
        val sut = createSUT()
        sut.update(1.0)

        timeSource += nextDuration()
        val read1 = sut.envelope!!
        timeSource += nextDuration()
        val read2 = sut.envelope!!

        assertEquals(read1, read2, 1e-9)
    }

    @Test
    fun `reading the envelope does not affect subsequent decay`() {
        val sut = createSUT()
        sut.update(1.0)

        timeSource += releaseHalfLife
        sut.envelope
        timeSource += releaseHalfLife
        val actual = sut.update(0.0)

        // Two release half-lives since the update
        assertEquals(0.25, actual, 1e-9)
    }

    // Attack
    @Test
    fun `rises along the exponential curve at fractional attack half-lives`() {
        val sut = createSUT()
        sut.update(0.0)

        timeSource += (attackHalfLife / 2)
        val actual = sut.update(1.0)

        assertEquals(1.0 - 2.0.pow(-0.5), actual, 1e-9)
    }

    @Test
    fun `rises halfway to a new value after one attack half-life`() {
        val sut = createSUT()
        sut.update(0.0)

        timeSource += attackHalfLife
        val actual = sut.update(1.0)

        assertEquals(0.5, actual, 1e-9)
    }

    @Test
    fun `rises along the exponential curve beyond one attack half-life`() {
        val sut = createSUT()
        sut.update(0.0)

        timeSource += (attackHalfLife * 2)
        val actual = sut.update(1.0)

        assertEquals(0.75, actual, 1e-9)
    }

    @Test
    fun `when attack is zero, a larger value takes effect immediately`() {
        val sut = createSUT(attackHalfLife = Duration.ZERO)
        sut.update(1.0)

        val actual = sut.update(5.0)

        assertEquals(5.0, actual)
    }

    @Test
    fun `when attack is zero, a smaller value falls at the release half-life`() {
        val sut = createSUT(attackHalfLife = Duration.ZERO)
        sut.update(1.0)

        timeSource += releaseHalfLife
        val actual = sut.update(0.0)

        assertEquals(0.5, actual, 1e-9)
    }

    // Release
    @Test
    fun `falls along the exponential curve at fractional release half-lives`() {
        val sut = createSUT()
        sut.update(1.0)

        timeSource += (releaseHalfLife / 2)
        val actual = sut.update(0.0)

        assertEquals(2.0.pow(-0.5), actual, 1e-9)
    }

    @Test
    fun `falls halfway to a new value after one release half-life`() {
        val sut = createSUT()
        sut.update(1.0)

        timeSource += releaseHalfLife
        val actual = sut.update(0.0)

        assertEquals(0.5, actual, 1e-9)
    }

    @Test
    fun `falls along the exponential curve beyond one release half-life`() {
        val sut = createSUT()
        sut.update(1.0)

        timeSource += (releaseHalfLife * 2)
        val actual = sut.update(0.0)

        assertEquals(0.25, actual, 1e-9)
    }

    @Test
    fun `falls toward the new value, not toward zero`() {
        val sut = createSUT()
        sut.update(1.0)

        timeSource += releaseHalfLife
        val actual = sut.update(0.6)

        // One release half-life covers half the remaining distance to the input:
        // 1.0 -> 0.8, on the way to 0.6. Decay toward zero would have given max(0.6, 0.5) = 0.6.
        assertEquals(0.8, actual, 1e-9)
    }

    @Test
    fun `when release is zero, a smaller value takes effect immediately`() {
        val sut = createSUT(releaseHalfLife = Duration.ZERO)
        sut.update(1.0)

        val actual = sut.update(0.3)

        assertEquals(0.3, actual)
    }

    // Direction change
    @Test
    fun `a subsequent strong peak interrupts the release immediately`() {
        val sut = createSUT(attackHalfLife = Duration.ZERO)

        // Strong peak
        val update1 = sut.update(1.0)
        assertEquals(1.0, update1, 1e-9)

        // Elapse one half life of silence
        timeSource += releaseHalfLife
        val update2 = sut.update(0.0)
        assertEquals(0.5, update2, 1e-9)

        // Fairly strong peak after another half life
        timeSource += releaseHalfLife
        val update3 = sut.update(0.8)
        assertEquals(0.8, update3, 1e-9)
    }

    @Test
    fun `a subsequent weak peak becomes the release target`() {
        val sut = createSUT(attackHalfLife = Duration.ZERO)

        // Strong peak
        val update1 = sut.update(1.0)
        assertEquals(1.0, update1, 1e-9)

        // Elapse one half life of silence
        timeSource += releaseHalfLife
        val update2 = sut.update(0.0)
        assertEquals(0.5, update2, 1e-9)

        // Softer peak after another half life
        timeSource += releaseHalfLife
        val update3 = sut.update(0.3)
        assertEquals(0.4, update3, 1e-9) // halfway between 0.5 and 0.3 is 0.4
    }

    // Convergence
    @Test
    fun `converges to a sustained new value`() {
        val sut = createSUT()
        sut.update(1.0)

        repeat(20) {
            timeSource += releaseHalfLife
            sut.update(0.5)
        }

        assertEquals(0.5, sut.envelope!!, 1e-6)
    }

    // Rate independence
    @Test
    fun `constant input remains constant regardless of update rate`() {
        val sut = createSUT()
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
    fun `immediate repeated observation does not move the envelope`() {
        val sut = createSUT()
        sut.update(1.0)

        val actual = sut.update(5.0)

        // Zero elapsed time means the new observation has zero weight
        assertEquals(1.0, actual, 1e-9)
    }

}
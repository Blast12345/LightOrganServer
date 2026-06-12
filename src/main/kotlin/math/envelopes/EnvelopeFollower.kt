package math.envelopes

import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * Follows the envelope of a signal: rises toward larger values at the attack rate
 * and falls toward smaller values at the release rate.
 *
 * Both directions decay exponentially, parameterized by half-life: the remaining
 * distance to the input halves with every half-life that passes. A half-life of
 * [Duration.ZERO] makes that direction instantaneous — an attack of zero yields
 * classic peak-detector behavior (never under-reports a peak, eases back down).
 *
 * Observations may arrive at irregular intervals; the response is computed from
 * elapsed time, so the trajectory is independent of update rate.
 */
class EnvelopeFollower(
    private val attackHalfLife: Duration,
    private val releaseHalfLife: Duration,
    private val timeSource: TimeSource = TimeSource.Monotonic
) {

    init {
        require(!attackHalfLife.isNegative()) { "attackHalfLife must not be negative, was $attackHalfLife" }
        require(!releaseHalfLife.isNegative()) { "releaseHalfLife must not be negative, was $releaseHalfLife" }
    }

    /** The current envelope, or null if no value has been observed yet. */
    var envelope: Double? = null
        private set

    private var lastObservation: TimeMark? = null

    /**
     * Incorporates a new observation and returns the updated envelope.
     *
     * The first observation seeds the envelope directly. Subsequent observations
     * pull the envelope toward themselves — at the attack rate when above it,
     * at the release rate when below it.
     */
    fun update(value: Double): Double {
        val previous = envelope
        val elapsed = lastObservation?.elapsedNow()
        lastObservation = timeSource.markNow()

        val updated = if (previous == null || elapsed == null) {
            value
        } else {
            val halfLife = if (value > previous) attackHalfLife else releaseHalfLife
            previous + weight(elapsed, halfLife) * (value - previous)
        }

        envelope = updated
        return updated
    }

    private fun weight(elapsed: Duration, halfLife: Duration): Double {
        if (halfLife == Duration.ZERO) return 1.0
        return 1.0 - 2.0.pow(-(elapsed / halfLife))
    }

}
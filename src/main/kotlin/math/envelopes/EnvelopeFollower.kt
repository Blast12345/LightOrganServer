package math.envelopes

import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.TimeMark
import kotlin.time.TimeSource

class EnvelopeFollower(
    private val attackHalfLife: Duration,
    private val releaseHalfLife: Duration,
    private val timeSource: TimeSource = TimeSource.Monotonic
) {

    init {
        require(!attackHalfLife.isNegative()) { "Attack half life must not be negative, was $attackHalfLife" }
        require(!releaseHalfLife.isNegative()) { "Release have life must not be negative, was $releaseHalfLife" }
    }

    var envelope: Double? = null
        private set

    private var lastObservation: TimeMark? = null

    fun update(value: Double): Double {
        val previous = envelope
        val elapsed = lastObservation?.elapsedNow()
        lastObservation = timeSource.markNow()

        val updated = if (previous == null || elapsed == null) {
            value
        } else {
            val halfLife = if (value > previous) attackHalfLife else releaseHalfLife
            val weight = weight(elapsed, halfLife)
            previous * (1.0 - weight) + value * weight
        }

        envelope = updated
        return updated
    }

    private fun weight(elapsed: Duration, halfLife: Duration): Double {
        if (halfLife == Duration.ZERO) return 1.0
        return 1.0 - 2.0.pow(-(elapsed / halfLife))
    }

}
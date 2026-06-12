package math.averages

import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.TimeMark
import kotlin.time.TimeSource

class ExponentialMovingAverage<T>(
    private val halfLife: Duration,
    private val scale: (T, Double) -> T,
    private val add: (T, T) -> T,
    private val timeSource: TimeSource = TimeSource.Monotonic
) {

    private var lastObservation: TimeMark? = null

    var average: T? = null
        private set

    init {
        require(halfLife.isPositive()) { "Half life must be positive, but was $halfLife" }
    }

    fun update(value: T): T {
        val previous = average
        val elapsed = lastObservation?.elapsedNow()
        lastObservation = timeSource.markNow()

        val updated = if (previous == null || elapsed == null) {
            value
        } else {
            val decay = 2.0.pow(-(elapsed / halfLife))
            add(scale(previous, decay), scale(value, 1.0 - decay))
        }

        average = updated
        return updated
    }

}
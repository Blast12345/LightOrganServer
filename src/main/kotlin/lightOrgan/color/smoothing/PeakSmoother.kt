package math.smoothing

import kotlin.math.pow
import kotlin.time.Duration

class PeakSmoother(
    private val halfLife: Duration
) : Smoother<Double> {

    private var current = 0.0
    private var lastTimestamp: Long? = null

    override fun smooth(value: Double): Double {
        val now = System.currentTimeMillis()
        val elapsed = lastTimestamp?.let { now - it } ?: 0L
        lastTimestamp = now

        val decay = 2.0.pow(-elapsed.toDouble() / halfLife.inWholeMilliseconds)
        current = maxOf(value, current * decay)

        return current
    }

}
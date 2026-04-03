package math.smoothing

import kotlin.math.pow
import kotlin.time.Duration

class PeakSmoother(
    private val halfLife: Duration
) {

    private var current = 0f
    private var lastTimestamp: Long? = null

    fun smooth(value: Float): Float {
        val now = System.currentTimeMillis()
        val elapsed = lastTimestamp?.let { now - it } ?: 0L
        lastTimestamp = now

        val decay = 2.0.pow(-elapsed.toDouble() / halfLife.inWholeMilliseconds).toFloat()
        current = maxOf(value, current * decay)

        return current
    }

}
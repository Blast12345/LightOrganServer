package math.smoothing

import kotlin.math.pow
import kotlin.time.Duration

class ExponentialSmoother<T>(
    private val halfLife: Duration,
    private val zero: T,
    private val scale: (T, Float) -> T,
    private val add: (T, T) -> T
) : Smoother<T> {

    private var smoothed: T = zero
    private var lastTimestamp: Long? = null

    override fun smooth(value: T): T {
        val now = System.currentTimeMillis()
        val elapsed = lastTimestamp?.let { now - it } ?: 0L
        lastTimestamp = now

        val decay = 2.0.pow(-elapsed.toDouble() / halfLife.inWholeMilliseconds).toFloat()
        smoothed = add(scale(smoothed, decay), value)

        return smoothed
    }

}
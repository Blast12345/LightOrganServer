package math.smoothing

import math.averages.ExponentialMovingAverage
import math.physics.Light
import kotlin.time.Duration
import kotlin.time.TimeSource

fun interface Smoother<T> {
    fun smooth(value: T): T
}


object Smoothers {

    fun lightExponentialMovingAverage(
        halfLife: Duration,
        timeSource: TimeSource = TimeSource.Monotonic
    ): Smoother<Light> {
        val average = ExponentialMovingAverage<Light>(
            halfLife = halfLife,
            scale = { light, factor -> light * factor },
            add = { first, second -> first + second },
            timeSource = timeSource
        )

        return Smoother(average::update)
    }

}
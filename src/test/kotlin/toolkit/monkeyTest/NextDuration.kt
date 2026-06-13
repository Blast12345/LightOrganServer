package toolkit.monkeyTest

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

fun nextDuration(
    min: Duration = 1.milliseconds,
    max: Duration = 1.seconds
): Duration {
    return nextInt(
        min = min.inWholeMilliseconds.toInt(),
        max = max.inWholeMilliseconds.toInt()
    ).milliseconds
}
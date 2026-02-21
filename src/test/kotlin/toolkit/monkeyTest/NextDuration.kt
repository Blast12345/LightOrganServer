package toolkit.monkeyTest

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun nextDuration(): Duration {
    return nextPositiveInt().milliseconds
}
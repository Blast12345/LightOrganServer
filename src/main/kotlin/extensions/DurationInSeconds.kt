package extensions

import kotlin.time.Duration
import kotlin.time.DurationUnit

val Duration.inSeconds: Float
    get() = toDouble(DurationUnit.SECONDS).toFloat()
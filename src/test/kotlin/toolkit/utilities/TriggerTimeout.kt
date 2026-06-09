package toolkit.utilities

import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.milliseconds

suspend fun triggerTimeout(): Nothing {
    withTimeout(1.milliseconds) { delay(2.milliseconds) }
    error("unreachable")
}
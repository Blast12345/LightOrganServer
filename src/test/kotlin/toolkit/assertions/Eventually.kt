package toolkit.assertions

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun eventually(timeout: Duration = 1.seconds, assertion: () -> Unit) {
    val deadline = System.nanoTime() + timeout.inWholeNanoseconds
    while (true) {
        try {
            assertion()
            return
        } catch (e: AssertionError) {
            if (System.nanoTime() > deadline) throw e
            Thread.sleep(10)
        }
    }
}
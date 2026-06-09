package utilities

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import logging.Logger

data class Sequenced<T>(
    val label: String,
    val sequenceNumber: Long,
    val value: T
)

fun <T> T.asSequenced(label: String, sequenceNumber: Long): Sequenced<T> {
    return Sequenced(label, sequenceNumber, value = this)
}

// TODO: Test me?
// TODO: When to reset?
fun <T, R> Flow<Sequenced<T>>.mapSequenced(
    label: String,
    transform: suspend (T) -> R
): Flow<Sequenced<R>> {
    var expected: Long? = null
    var outgoing = 0L

    return map { incoming ->
        val exp = expected ?: incoming.sequenceNumber
        val delta = incoming.sequenceNumber - exp
        expected = incoming.sequenceNumber + 1

        if (delta > 0) {
            Logger.warning("$label is slow, dropped $delta")
        }

        Sequenced(label, outgoing++, transform(incoming.value))
    }
}
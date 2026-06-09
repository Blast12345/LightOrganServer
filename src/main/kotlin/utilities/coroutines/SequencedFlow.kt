package utilities.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

data class Sequenced<T>(
    val sequenceNumber: Long,
    val value: T
)

fun <T> T.asSequenced(sequenceNumber: Long): Sequenced<T> {
    return Sequenced(sequenceNumber, value = this)
}

fun <T, R> Flow<Sequenced<T>>.mapSequenced(
    onGap: (size: Long) -> Unit = {},
    transform: suspend (T) -> R
): Flow<Sequenced<R>> {
    val gapDetector = SequenceGapDetector()
    var outgoingNumber = 0L

    return map { incoming ->
        gapDetector.check(incoming.sequenceNumber, onGap)
        Sequenced(outgoingNumber++, transform(incoming.value))
    }
}

fun <T> Flow<Sequenced<T>>.onEachSequenced(
    onGap: (size: Long) -> Unit = {},
    action: suspend (T) -> Unit
): Flow<Sequenced<T>> {
    val gapDetector = SequenceGapDetector()

    return onEach { incoming ->
        gapDetector.check(incoming.sequenceNumber, onGap)
        action(incoming.value)
    }
}
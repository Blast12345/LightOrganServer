package utilities.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

fun <T> Flow<Sequenced<T>>.onEachSequenced(
    gapDetector: SequenceGapDetector = SequenceGapDetector(),
    onGap: (size: Long) -> Unit = {},
    action: suspend (T) -> Unit
): Flow<Sequenced<T>> {
    return onEach { incoming ->
        val gap = gapDetector.check(incoming.sequenceNumber)

        if (gap != 0L) {
            onGap(gap)
        }

        action(incoming.value)
    }
}
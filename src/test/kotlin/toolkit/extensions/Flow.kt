package toolkit.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T> Flow<T>.collectInto(scope: CoroutineScope): MutableList<T> {
    val received = mutableListOf<T>()
    onEach { received.add(it) }.launchIn(scope)
    return received
}
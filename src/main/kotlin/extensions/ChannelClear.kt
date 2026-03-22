package extensions

import kotlinx.coroutines.channels.Channel

fun <T> Channel<T>.clear() {
    while (tryReceive().isSuccess) { /* discard */
    }
}
package extensions

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

suspend fun <T> StateFlow<T>.await(value: T) {
    first { it == value }
}
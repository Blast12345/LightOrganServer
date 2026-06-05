package toolkit.extensions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.collectInto(scope: TestScope): MutableList<T> {
    val received = mutableListOf<T>()
    onEach { received.add(it) }.launchIn(scope.backgroundScope)
    scope.runCurrent()
    return received
}
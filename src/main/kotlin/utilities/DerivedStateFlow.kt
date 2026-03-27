package utilities

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

class DerivedStateFlow<T, R>(
    private val source: StateFlow<T>,
    private val transform: (T) -> R
) : StateFlow<R> {

    override val value: R get() = transform(source.value)

    override val replayCache: List<R> get() = listOf(value)

    override suspend fun collect(collector: FlowCollector<R>): Nothing {
        source.collect { collector.emit(transform(it)) }
    }

}
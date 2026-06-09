package utilities.coroutines

fun <T> T.asLazySequence(): Sequenced<T> = Sequenced(-1L, this)
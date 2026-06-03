package extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import logging.Logger
import kotlin.coroutines.cancellation.CancellationException

inline fun CoroutineScope.tryLaunch(
    crossinline block: suspend () -> Unit,
    crossinline onError: (Exception) -> Unit,
) = launch {
    try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Logger.error(e)
        onError(e)
    }
}
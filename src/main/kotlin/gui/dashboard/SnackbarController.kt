package gui.dashboard

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

interface SnackbarController {
    suspend fun show(message: String)
}

// TODO: Extract me?
// TODO: Update other tests
class SharedFlowSnackbarController : SnackbarController {
    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> = _messages.asSharedFlow()

    override suspend fun show(message: String) {
        _messages.emit(message)
    }
}
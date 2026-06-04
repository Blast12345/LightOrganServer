package gui.dashboard.snackbar

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

// TODO: Update other tests
// TODO: Test me
class SharedFlowSnackbarController : SnackbarController {

    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> = _messages.asSharedFlow()

    override fun show(message: String) {
        _messages.tryEmit(message)
    }

}
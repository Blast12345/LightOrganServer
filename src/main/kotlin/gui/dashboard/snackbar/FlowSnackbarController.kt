package gui.dashboard.snackbar

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class FlowSnackbarController : SnackbarController {

    private val _messages = Channel<String>(capacity = Channel.UNLIMITED)
    val messages: Flow<String> = _messages.receiveAsFlow()

    override fun show(message: String) {
        _messages.trySend(message)
    }

}
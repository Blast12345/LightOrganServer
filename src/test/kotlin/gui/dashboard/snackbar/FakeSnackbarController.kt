package gui.dashboard.snackbar

class FakeSnackbarController : SnackbarController {

    var lastMessage: String? = null

    override fun show(message: String) {
        lastMessage = message
    }

}
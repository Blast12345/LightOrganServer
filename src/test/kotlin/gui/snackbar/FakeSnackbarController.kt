package gui.snackbar

class FakeSnackbarController : SnackbarController {

    var lastMessage: String? = null

    override fun show(message: String) {
        lastMessage = message
    }

}
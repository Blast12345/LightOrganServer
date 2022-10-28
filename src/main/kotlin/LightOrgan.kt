import colorService.ColorService
import colorService.ColorServiceInterface
import server.Server
import server.ServerInterface

class LightOrgan(private var server: ServerInterface = Server(),
                 private var colorService: ColorServiceInterface = ColorService()) {

    var isRunning = false
        private set
    private var colorsPerSecond = 60

    fun start() {
        isRunning = true
        startListeningForNextColor()
    }

    private fun startListeningForNextColor() {
        colorService.listenForNextColor { rgbString ->
            sendMessageIfAble(rgbString)
        }
    }

    private fun sendMessageIfAble(rgbString: String) {
        if (shouldSendMessage()) {
            server.sendMessage(rgbString)
        }
    }

    private fun shouldSendMessage(): Boolean {
        val millisecondsSinceLastSentMessage = server.millisecondsSinceLastSentMessage

        return if (millisecondsSinceLastSentMessage != null) {
            millisecondsSinceLastSentMessage >= minimumColorDurationInMilliseconds()
        } else {
            true
        }
    }

    private fun minimumColorDurationInMilliseconds(): Long {
        val minimumColorDurationInMilliseconds = 1 / colorsPerSecond.toFloat() * 1000
        return minimumColorDurationInMilliseconds.toLong()
    }

}
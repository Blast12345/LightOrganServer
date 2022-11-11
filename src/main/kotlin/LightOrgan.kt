import colorService.ColorService
import colorService.ColorServiceInterface
import server.Server
import server.ServerInterface
import java.awt.Color

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
        colorService.listenForNextColor { color ->
            sendColorIfAble(color)
        }
    }

    private fun sendColorIfAble(color: Color) {
        if (shouldSendColor()) {
            server.sendColor(color)
        }
    }

    private fun shouldSendColor(): Boolean {
        val millisecondsSinceLastSentColor = server.millisecondsSinceLastSentColor

        return if (millisecondsSinceLastSentColor != null) {
            millisecondsSinceLastSentColor >= minimumColorDurationInMilliseconds()
        } else {
            true
        }
    }

    private fun minimumColorDurationInMilliseconds(): Long {
        val minimumColorDurationInMilliseconds = 1 / colorsPerSecond.toFloat() * 1000
        return minimumColorDurationInMilliseconds.toLong()
    }

}
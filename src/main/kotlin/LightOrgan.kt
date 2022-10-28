import colorService.ColorService
import colorService.ColorServiceInterface
import server.Server
import server.ServerInterface

class LightOrgan(var server: ServerInterface = Server(),
                 var colorService: ColorServiceInterface = ColorService()) {

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

//
// SAMPLE CODE
//
//var lastInMilliseconds = System.currentTimeMillis()
//var updatesPerSecond = 60.0
//var updateDelay = 1.0 / updatesPerSecond * 1000
//
//var red = 1
//var redAscending = true
//var green = 1
//var greenAscending = true
//var blue = 1
//var blueAscending = true
//
//while (true) {
//    val currentTimeMilliseconds = System.currentTimeMillis()
//
//    if (currentTimeMilliseconds - lastInMilliseconds > updateDelay) {
//        server.sendMessage("$red,$green,$blue")
//        lastInMilliseconds = currentTimeMilliseconds
//
//        if (redAscending) {
//            red += 1
//        } else {
//            red -= 1
//        }
//
//        if (red == 255) {
//            redAscending = false
//        } else if (red == 1) {
//            redAscending = true
//        }
//
//        if (greenAscending) {
//            green += 2
//        } else {
//            green -= 2
//        }
//
//        if (green == 253) {
//            greenAscending = false
//        } else if (green == 1) {
//            greenAscending = true
//        }
//
//        if (blueAscending) {
//            blue += 3
//        } else {
//            blue -= 3
//        }
//
//        if (blue == 253) {
//            blueAscending = false
//        } else if (blue == 1) {
//            blueAscending = true
//        }
//    }
//}
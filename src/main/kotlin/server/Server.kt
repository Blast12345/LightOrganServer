package server

import java.awt.Color

interface ServerInterface {
    var lastColorTimestampInMilliseconds: Long?
    var millisecondsSinceLastSentColor: Long?
    fun sendColor(color: Color)
}

// TODO: Start listening for clients; probably using a client manager.
class Server(
    private val timeUtility: TimeUtilityInterface = TimeUtility(),
    private val socket: UdpSocketInterface = UdpSocket()): ServerInterface {

    private val address = "192.168.1.55"
    private val port = 9999

    override var lastColorTimestampInMilliseconds: Long? = null
    override var millisecondsSinceLastSentColor: Long? = null
        get() = getMillisecondsSinceLastSentColor(lastColorTimestampInMilliseconds)

    private fun getMillisecondsSinceLastSentColor(lastColorTimestampInMilliseconds: Long?): Long? {
        return if (lastColorTimestampInMilliseconds != null) {
            timeUtility.currentTimeMilliseconds() - lastColorTimestampInMilliseconds
        } else {
            null
        }
    }

    override fun sendColor(color: Color) {
        val colorString = "${color.red},${color.green},${color.blue}"
        sendMessage(colorString)
        updateLastColorTimestamp()
    }

    private fun sendMessage(message: String) {
        socket.send(message, address, port)
    }

    private fun updateLastColorTimestamp() {
        lastColorTimestampInMilliseconds = timeUtility.currentTimeMilliseconds()
    }

}

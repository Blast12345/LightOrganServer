package server

interface ServerInterface {
    var lastMessageTimestampInMilliseconds: Long?
    var millisecondsSinceLastSentMessage: Long?
    fun sendMessage(message: String)
}

// TODO: Start listening for clients
// Maybe create a ClientManager?
class Server(
    private val timeUtility: TimeUtilityInterface = TimeUtility(),
    private val socket: UdpSocketInterface = UdpSocket()): ServerInterface {

    private val address = "192.168.1.55"
    private val port = 9999

    override var lastMessageTimestampInMilliseconds: Long? = null
    override var millisecondsSinceLastSentMessage: Long? = null
        get() = getMillisecondsSinceLastSentMessage(lastMessageTimestampInMilliseconds)

    private fun getMillisecondsSinceLastSentMessage(lastMessageTimestampInMilliseconds: Long?): Long? {
        return if (lastMessageTimestampInMilliseconds != null) {
            timeUtility.currentTimeMilliseconds() - lastMessageTimestampInMilliseconds
        } else {
            null
        }
    }

    override fun sendMessage(message: String) {
        sendPacketForMessage(message)
        updateLastMessageTimestamp()
        println("Sent Message: $message")
    }

    private fun sendPacketForMessage(message: String) {
        socket.send(message, address, port)
    }

    private fun updateLastMessageTimestamp() {
        lastMessageTimestampInMilliseconds = timeUtility.currentTimeMilliseconds()
    }

}

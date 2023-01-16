package server

import java.awt.Color

interface ServerInterface {
    fun sendColor(color: Color)
}

// TODO: Start listening for clients; probably using a client manager.
class Server(
    private val socket: UdpSocketInterface = UdpSocket()
) : ServerInterface {

    private val address = "192.168.1.55"
    private val port = 9999

    override fun sendColor(color: Color) {
        val colorString = "${color.red},${color.green},${color.blue}"
        sendMessage(colorString)
    }

    private fun sendMessage(message: String) {
        socket.send(message, address, port)
    }

}

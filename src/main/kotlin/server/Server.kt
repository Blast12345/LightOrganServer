package server

import config.Config
import java.awt.Color

interface ServerInterface {
    fun sendColor(color: Color)
}

class Server(
    config: Config,
    private val socket: UdpSocketInterface = UdpSocket()
) : ServerInterface {

    private val clients = config.clients
    private var timestampOfLastSentColor = System.currentTimeMillis()

    override fun sendColor(color: Color) {
        // TODO: ColorStringFactory?
        val colorString = "${color.red},${color.green},${color.blue}"
        sendMessage(colorString)
        printServerLatency()
    }

    private fun sendMessage(message: String) {
        for (client in clients) {
            socket.send(message, client)
        }
    }

    private fun printServerLatency() {
        val timeBetweenColors = System.currentTimeMillis() - timestampOfLastSentColor
        println("Server Latency: $timeBetweenColors")
        timestampOfLastSentColor = System.currentTimeMillis()
    }

}

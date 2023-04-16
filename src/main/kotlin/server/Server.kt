package server

import config.children.Client
import java.awt.Color

interface ServerInterface {
    fun sendColor(color: Color)
}

// TODO: I should double check that UDP is significantly faster than TCP for my use case.
class Server(
    private val clients: List<Client>,
    private val socket: UdpSocketInterface = UdpSocket(),
    private val colorMessageFactory: ColorMessageFactory = ColorMessageFactory()
) : ServerInterface {

    private var timestampOfLastSentColor = System.currentTimeMillis()

    override fun sendColor(color: Color) {
        val colorMessage = colorMessageFactory.create(color)
        sendMessage(colorMessage)
    }

    private fun sendMessage(message: String) {
        for (client in clients) {
            socket.send(message, client)
        }

        printServerLatency()
    }

    private fun printServerLatency() {
        val timeBetweenColors = System.currentTimeMillis() - timestampOfLastSentColor
        println("Server Latency: $timeBetweenColors")
        timestampOfLastSentColor = System.currentTimeMillis()
    }

}

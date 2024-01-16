package server

import config.Config
import config.ConfigSingleton
import config.children.Client
import lightOrgan.LightOrganSubscriber
import wrappers.color.Color

class Server(
    private val config: Config = ConfigSingleton,
    private val socket: UdpSocket = UdpSocket(),
    private val colorMessageFactory: ColorMessageFactory = ColorMessageFactory()
) : LightOrganSubscriber {

    private var timestampOfLastSentColor = System.currentTimeMillis()

    override fun new(color: Color) {
        val colorMessage = colorMessageFactory.create(color)
        sendMessage(colorMessage)
    }

    private fun sendMessage(message: String) {
        for (client in clients) {
            socket.send(message, client)
        }

        printServerLatency()
    }

    private val clients: Set<Client>
        get() = config.clients

    private fun printServerLatency() {
        val timeBetweenColors = System.currentTimeMillis() - timestampOfLastSentColor
        println("Server Latency: $timeBetweenColors")
        timestampOfLastSentColor = System.currentTimeMillis()
    }

}

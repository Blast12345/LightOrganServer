package server

import config.Config
import config.ConfigProvider
import lightOrgan.LightOrganSubscriber
import java.awt.Color

// TODO: I should double check that UDP is significantly faster than TCP for my use case.
class Server(
    private val config: Config = ConfigProvider().current,
    private val socket: UdpSocketInterface = UdpSocket(),
    private val colorMessageFactory: ColorMessageFactory = ColorMessageFactory()
) : LightOrganSubscriber {

    private var timestampOfLastSentColor = System.currentTimeMillis()

    override fun new(color: Color) {
        val colorMessage = colorMessageFactory.create(color)
        sendMessage(colorMessage)
    }

    private fun sendMessage(message: String) {
        for (client in config.clients) {
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

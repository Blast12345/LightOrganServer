package server

import androidx.compose.ui.graphics.Color
import config.Config
import config.ConfigSingleton
import config.children.Client


class Server(
    private val config: Config = ConfigSingleton,
    private val socket: UdpSocket = UdpSocket(),
    private val colorMessageFactory: ColorMessageFactory = ColorMessageFactory()
) {

    fun new(color: Color) {
        val colorMessage = colorMessageFactory.create(color)
        sendMessage(colorMessage)
    }

    private fun sendMessage(message: String) {
        for (client in clients) {
            socket.send(message, client)
        }
    }

    private val clients: Set<Client>
        get() = config.clients

}

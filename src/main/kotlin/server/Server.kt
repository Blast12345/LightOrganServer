package server

import androidx.compose.ui.graphics.Color
import config.Config
import config.ConfigSingleton
import config.children.Client

class Server(
    private val config: Config = ConfigSingleton,
    private val socket: UdpSocket = UdpSocket()
) {

    fun new(color: Color) {
        val red = (color.red * 255).toInt()
        val green = (color.green * 255).toInt()
        val blue = (color.blue * 255).toInt()
        sendMessage("$red,$green,$blue")
    }

    private fun sendMessage(message: String) {
        for (client in clients) {
            socket.send(message, client)
        }
    }

    private val clients: Set<Client>
        get() = config.clients

}
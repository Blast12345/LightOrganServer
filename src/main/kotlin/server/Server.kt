package server

import color.StandardRgbColor
import config.Config
import config.ConfigSingleton
import config.children.Client

class Server(
    private val config: Config = ConfigSingleton,
    private val socket: UdpSocket = UdpSocket()
) {

    fun new(color: StandardRgbColor) {
        val red = (color.red.value * 255).toInt()
        val green = (color.green.value * 255).toInt()
        val blue = (color.blue.value * 255).toInt()
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
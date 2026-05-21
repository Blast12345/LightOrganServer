package server

import color.SrgbColor
import config.Config
import config.ConfigSingleton
import config.children.Client
import kotlin.math.roundToInt

class Server(
    private val config: Config = ConfigSingleton,
    private val socket: UdpSocket = UdpSocket()
) {

    fun new(color: SrgbColor) {
        val red = (color.red.value * 255).roundToInt()
        val green = (color.green.value * 255).roundToInt()
        val blue = (color.blue.value * 255).roundToInt()
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
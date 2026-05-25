package server

//class Server(
//    private val config: Config = ConfigSingleton,
//    private val socket: UdpSocket = UdpSocket()
//) {
//
//    fun new(color: StandardRgbColor) {
//        val red = (color.red.value * 255).roundToInt()
//        val green = (color.green.value * 255).roundToInt()
//        val blue = (color.blue.value * 255).roundToInt()
//        sendMessage("$red,$green,$blue")
//    }
//
//    private fun sendMessage(message: String) {
//        for (client in clients) {
//            socket.send(message, client)
//        }
//    }
//
//    private val clients: Set<Client>
//        get() = config.clients
//
//}
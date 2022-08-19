import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.sql.Time

fun main() {
    val server = Server()

    var lastInSeconds = System.currentTimeMillis() / 1000
    while (true) {
        val currentTimeSeconds = System.currentTimeMillis() / 1000

        if (currentTimeSeconds - lastInSeconds > 5) {
            server.sendMessage("Hello")
            lastInSeconds = currentTimeSeconds
        }
    }

//    val lineIn = TargetLineIn()
//    val lineInListener = LineInListener(lineIn)
//    val frequencyBinGenerator = FrequencyBinGenerator(lineInListener)
//    val frequencyBins = frequencyBinGenerator.getFrequencyBins()
}




// start server - this isn't testable in and of itself
// start listening - this isn't testable in and of itself
// get frequency bins
// generate the RGB commands given frequency bins and LED count
// send RGB commands to client(s)

class Server {

    private val socket = DatagramSocket()
    private val address = InetAddress.getByName("localhost")
    private val port = 9999

    fun sendMessage(message: String) {
        println("Sent Message: $message")
        val packet = createPacketForMessage(message)
        socket.send(packet)
    }

    private fun createPacketForMessage(message: String): DatagramPacket {
        val buf = message.toByteArray()
        return DatagramPacket(buf, buf.size, address, port)
    }

}

package server

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

interface UdpSocketInterface {
    fun send(message: String, address: String, port: Int)
}

class UdpSocket : UdpSocketInterface {

    private val socket = DatagramSocket()

    override fun send(message: String, address: String, port: Int) {
        val packet = createPacket(message, address, port)

        try {
            socket.send(packet)
        } catch (e: Exception) {
            // Don't do anything
        }

        println("Sent Message: $message")
    }

    private fun createPacket(message: String, address: String, port: Int): DatagramPacket {
        val buffer = message.toByteArray()
        return DatagramPacket(
            buffer,
            buffer.size,
            InetAddress.getByName(address),
            port
        )
    }

}


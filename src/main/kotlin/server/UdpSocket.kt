package server

import config.Client
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

// TODO: Test me?
class UdpSocket {

    private val socket = DatagramSocket()

    fun send(message: String, client: Client) {
        send(message, client.ip, client.port)
    }

    private fun send(message: String, address: String, port: Int) {
        val packet = createPacket(message, address, port)

        try {
            socket.send(packet)
        } catch (e: Exception) {
            // Don't do anything
        }

        println("Sent Message: $message")
    }

    private fun createPacket(message: String, address: String, port: Int): DatagramPacket {
        return createPacket(
            buffer = message.toByteArray(),
            address = address,
            port = port
        )
    }

    private fun createPacket(buffer: ByteArray, address: String, port: Int): DatagramPacket {
        return DatagramPacket(
            buffer,
            buffer.size,
            InetAddress.getByName(address),
            port
        )
    }

}


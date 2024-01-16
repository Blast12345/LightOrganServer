package server

import config.children.Client
import logging.Logger
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class UdpSocket {

    private val socket = DatagramSocket()

    fun send(message: String, client: Client) {
        send(message, client.ip, client.port)
    }

    private fun send(message: String, address: String, port: Int) {
        val packet = createPacket(message, address, port)

        try {
            socket.send(packet)
            Logger.success("UDP Socket send success: $message")
        } catch (e: IOException) {
            Logger.error("UDP Socket send failed: ${e.message}")
        }
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

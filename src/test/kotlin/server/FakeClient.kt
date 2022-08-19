package server

import java.net.DatagramPacket
import java.net.DatagramSocket

class FakeClient {

    private val socket = DatagramSocket(4445)
    private val buf = ByteArray(256)

    fun getMessage() : String {
        val packet = getNextPacket()
        return getMessageFrom(packet)
    }

    private fun getNextPacket() : DatagramPacket {
        val packet = DatagramPacket(buf, buf.size)
        socket.receive(packet)
        return packet
    }

    private fun getMessageFrom(packet: DatagramPacket) : String {
        return String(packet.data, 0, packet.length)
    }

    fun stop() {
        socket.close()
    }

}


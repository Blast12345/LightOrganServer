package server

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Color

class ServerTests {

    private lateinit var socket: FakeUdpSocket
    private val color = Color.blue

    @BeforeEach
    fun setup() {
        socket = FakeUdpSocket()
    }

    private fun createSUT(): Server {
        return Server(socket)
    }

    @Test
    fun `the server can send a color`() {
        val sut = createSUT()
        sut.sendColor(color)
        assertEquals(socket.message, "${color.red},${color.green},${color.blue}")
        // NOTE: We are assuming this is hardcoded for the MVP.
        assertEquals(socket.address, "192.168.1.55")
        assertEquals(socket.port, 9999)
    }

}
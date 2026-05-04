package server

import androidx.compose.ui.graphics.Color
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextClient
import toolkit.monkeyTest.nextConfig

class ServerTests {

    private val client1 = nextClient()
    private val client2 = nextClient()
    private val config = nextConfig(clients = setOf(client1, client2))
    private val socket: UdpSocket = mockk()

    @BeforeEach
    fun setupHappyPath() {
        every { socket.send(any(), any()) } returns Unit
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): Server {
        return Server(
            config = config,
            socket = socket
        )
    }

    @Test
    fun `when a new color is received then the server sends a string representation of that color`() {
        val sut = createSUT()
        val color = Color(255, 128, 64)

        sut.new(color)

        val colorMessage = "255,128,64"
        verify { socket.send(colorMessage, client1) }
        verify { socket.send(colorMessage, client2) }
    }

}

package lightOrgan.server

import config.Config
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextClient
import java.awt.Color

class ServerTests {

    private val config: Config = mockk()
    private val socket: UdpSocket = mockk()

    private val color = Color.blue
    private val colorString = "0,0,255"

    private val client1 = nextClient()
    private val client2 = nextClient()
    private val clients = listOf(client1, client2)

    @BeforeEach
    fun setup() {
        every { config.clients } returns clients
        every { socket.send(any(), any()) } returns Unit
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): Server {
        return Server(
            config = config,
            socket = socket
        )
    }

    @Test
    fun `the server sends the color to each client`() {
        val sut = createSUT()
        sut.sendColor(color)
        verify { socket.send(colorString, client1) }
        verify { socket.send(colorString, client2) }
    }

}
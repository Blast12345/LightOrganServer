package server

import config.Config
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextClient
import toolkit.monkeyTest.nextColor
import toolkit.monkeyTest.nextString

class ServerTests {

    private val config: Config = mockk()
    private val socket: UdpSocket = mockk()
    private val colorMessageFactory: ColorMessageFactory = mockk()

    private val client1 = nextClient()
    private val client2 = nextClient()
    private val clients = listOf(client1, client2)
    private val nextColorMessage = nextString()

    @BeforeEach
    fun setup() {
        every { config.clients } returns clients
        every { socket.send(any(), any()) } returns Unit
        every { colorMessageFactory.create(any()) } returns nextColorMessage
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): Server {
        return Server(
            config = config,
            socket = socket,
            colorMessageFactory = colorMessageFactory
        )
    }

    @Test
    fun `when a new color is received then the server sends a string representation of that color`() {
        val sut = createSUT()
        val color = nextColor()

        sut.new(color)

        verify { colorMessageFactory.create(color) }
        verify { socket.send(nextColorMessage, client1) }
        verify { socket.send(nextColorMessage, client2) }
    }

}
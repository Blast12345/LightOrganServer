package server

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import toolkit.monkeyTest.nextAwtColor
import toolkit.monkeyTest.nextClient
import toolkit.monkeyTest.nextString

class ServerTests {

    private val client1 = nextClient()
    private val client2 = nextClient()
    private val clients = listOf(client1, client2)
    private val socket: UdpSocket = mockk()
    private val colorMessageFactory: ColorMessageFactory = mockk()

    private val nextColorMessage = nextString()

    @BeforeEach
    fun setup() {
        every { socket.send(any(), any()) } returns Unit
        every { colorMessageFactory.create(any()) } returns nextColorMessage
    }

    @AfterEach
    fun teardown() {
        clearAllMocks()
    }

    private fun createSUT(): Server {
        return Server(
            clients = clients,
            socket = socket,
            colorMessageFactory = colorMessageFactory
        )
    }

    @Test
    fun `the server sends the color to each client`() {
        val sut = createSUT()
        val color = nextAwtColor()

        sut.sendColor(color)

        verify { socket.send(nextColorMessage, client1) }
        verify { socket.send(nextColorMessage, client2) }
    }

    @Test
    fun `the color message is created for the color we are sending`() {
        val sut = createSUT()
        val color = nextAwtColor()

        sut.sendColor(color)

        verify { colorMessageFactory.create(color) }
    }

}
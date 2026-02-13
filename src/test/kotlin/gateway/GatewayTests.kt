package gateway

import gateway.messages.GatewayIdentificationResponse
import gateway.messages.GatewaySetColorCommand
import gateway.messages.MessageFactory
import gateway.serial.SerialRouter
import gateway.serial.wrappers.SerialFormat
import gateway.serial.wrappers.SerialPort
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import toolkit.monkeyTest.nextGatewayIdentificationRequest
import toolkit.monkeyTest.nextGatewayIdentificationResponse
import toolkit.monkeyTest.nextString
import wrappers.color.Color

class GatewayTests {

    val port: SerialPort = mockk()
    val baudRate: Int = 115200
    val serialFormat = SerialFormat.FORMAT_8N1
    val serialRouter: SerialRouter = mockk()
    val serialRouterIsConnected = MutableStateFlow(false)
    val messageFactory: MessageFactory = mockk()

    val gatewayIdentificationRequest = nextGatewayIdentificationRequest()
    val gatewayIdentificationResponse = nextGatewayIdentificationResponse()

    val newColor: Color = mockk()
    val setColorCommand: GatewaySetColorCommand = mockk()

    val exception: Exception = mockk()

    @BeforeEach
    fun setupHappyPath() {
        mockkObject(SerialRouter.Companion)
        every { SerialRouter.create(port, baudRate, serialFormat) } returns serialRouter

        every { port.systemPath } returns nextString("systemPath")

        every { serialRouter.connect() } returns Unit
        every { serialRouter.isConnected } returns serialRouterIsConnected
        every { messageFactory.createIdentificationRequest() } returns gatewayIdentificationRequest
        coEvery {
            serialRouter.send(
                gatewayIdentificationRequest,
                GatewayIdentificationResponse::class.java
            )
        } returns gatewayIdentificationResponse

        every { serialRouter.disconnect() } returns Unit

        every { messageFactory.createColorCommand(newColor) } returns setColorCommand
        every { serialRouter.send(setColorCommand) } returns Unit
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    suspend fun connect(): Gateway {
        return Gateway.connect(port, messageFactory)
    }

    // Connect
    @Test
    fun `connect to a valid port`() = runTest {
        val gateway = connect()

        verify { serialRouter.connect() }
        assertEquals(port.systemPath, gateway.systemPath)
        assertEquals(gatewayIdentificationResponse.macAddress, gateway.macAddress)
        assertEquals(gatewayIdentificationResponse.firmwareVersion, gateway.firmwareVersion)
    }

    @Test
    fun `when connecting to the port fails, attempt disconnect and rethrow exception`() = runTest {
        every { serialRouter.connect() } throws exception

        val actual = assertThrows<Exception> {
            connect()
        }

        assertEquals(exception, actual)
        verify { serialRouter.disconnect() }
    }

    @Test
    fun `when handshaking fails, disconnect from the port and rethrow exception`() = runTest {
        coEvery {
            serialRouter.send(
                gatewayIdentificationRequest,
                GatewayIdentificationResponse::class.java
            )
        } throws exception

        val actual = assertThrows<Exception> {
            connect()
        }

        assertEquals(exception, actual)
        verify { serialRouter.disconnect() }
    }

    // Connection
    @Test
    fun `reconnect to the port`() = runTest {
        val sut = connect()

        sut.reconnect()

        verify { serialRouter.connect() }
    }

    @Test
    fun `disconnect from the port`() = runTest {
        val sut = connect()

        sut.disconnect()

        verify { serialRouter.disconnect() }
    }

    @Test
    fun `get the connection state`() = runTest {
        val sut = connect()

        val actual = sut.isConnected

        assertEquals(serialRouterIsConnected, actual)
    }

    // New color
    @Test
    fun `send a new color to the gateway`() = runTest {
        val sut = connect()

        sut.send(newColor)

        verify { serialRouter.send(setColorCommand) }
    }

}

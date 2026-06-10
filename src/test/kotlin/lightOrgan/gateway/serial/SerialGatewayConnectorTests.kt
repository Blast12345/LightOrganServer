package lightOrgan.gateway.serial

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import jsonrpc.FakeJsonRpcConnection
import kotlinx.coroutines.test.runTest
import lightOrgan.gateway.FakeGateway
import lightOrgan.gateway.GatewayFactory
import lightOrgan.gateway.SerialGatewayDetails
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import serial.FakeSerialPort
import serial.SerialFrameFormat
import toolkit.monkeyTest.*
import kotlin.time.Duration

class FakeSerialJsonRpcConnection : FakeJsonRpcConnection(), SerialJsonRpcConnection {

    override val name: String = nextString("name")
    override val baudRate: Int = nextInt()
    override val frameFormat: SerialFrameFormat = nextSerialFrameFormat()

}

class SerialGatewayConnectorTests {

    private val connectionFactory: SerialJsonRpcConnectionFactory = mockk()
    private val gatewayFactory: GatewayFactory = mockk()
    private val timeout: Duration = nextDuration()

    private lateinit var gatewayPort: FakeSerialPort
    private lateinit var notGatewayPort: FakeSerialPort
    private lateinit var fakeConnection: FakeSerialJsonRpcConnection
    private lateinit var fakeGateway: FakeGateway

    private val identificationResponse = nextGatewayIdentificationResponse()

    @BeforeEach
    fun setupHappyPath() {
        gatewayPort = FakeSerialPort()
        notGatewayPort = FakeSerialPort()
        fakeConnection = FakeSerialJsonRpcConnection()
        fakeGateway = FakeGateway()

        every { connectionFactory.create(any()) } returns fakeConnection

        every {
            gatewayFactory.create(
                details = SerialGatewayDetails(
                    macAddress = identificationResponse.macAddress,
                    firmwareVersion = identificationResponse.firmwareVersion,
                    baudRate = fakeConnection.baudRate,
                    frameFormat = fakeConnection.frameFormat
                ),
                connection = fakeConnection
            )
        } returns fakeGateway
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): SerialGatewayConnector {
        return SerialGatewayConnector(
            connectionFactory = connectionFactory,
            gatewayFactory = gatewayFactory,
            timeout = timeout
        )
    }

    @Test
    fun `connect to the gateway`() = runTest {
        val sut = createSUT()
        fakeConnection.stubResponse("gateway-identification-request", null, timeout, identificationResponse)

        val gateway = sut.connect(gatewayPort)

        assertEquals(fakeGateway, gateway)
    }

    @Test
    fun `when a port fails to connect, then throw`() = runTest {
        val sut = createSUT()
        fakeConnection.connectError = nextException()

        assertThrows<Exception> { sut.connect(gatewayPort) }
    }

    @Test
    fun `when the handshake fails, then throw`() = runTest {
        val sut = createSUT()
        fakeConnection.sendRequestError = nextException()

        assertThrows<Exception> { sut.connect(gatewayPort) }
        assertEquals(false, fakeConnection.isConnected.value)
    }

}
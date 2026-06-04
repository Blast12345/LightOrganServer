//package lightOrgan.gateway
//
//import io.mockk.*
//import kotlinx.coroutines.test.runTest
//import lightOrgan.gateway.serial.SerialGateway
//import lightOrgan.gateway.serial.SerialGatewayConnector
//import lightOrgan.gateway.serial.SerialGatewayFactory
//import lightOrgan.gateway.serial.messages.GatewayIdentificationResponse
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertNull
//import serial.rpc.DefaultSerialRpcClient
//import toolkit.monkeyTest.nextException
//import toolkit.monkeyTest.nextGatewayIdentificationResponse
//
//class SerialGatewayConnectorTests {
//
//    private val gatewayFactory: SerialGatewayFactory = mockk()
//
//    private val client: DefaultSerialRpcClient = mockk()
//    private val identificationCommand = "gateway-identification" // TODO: Enum?
//    private val identificationResponse = nextGatewayIdentificationResponse()
//    private val gateway: SerialGateway = mockk()
//
//    @BeforeEach
//    fun setupHappyPath() {
//        every { client.connect() } returns Unit
//        coEvery { client.request(identificationCommand, any(), GatewayIdentificationResponse::class.java) } returns identificationResponse
//        every { client.disconnect() } returns Unit
//
//        every { gatewayFactory.create(identificationResponse, client) } returns gateway
//    }
//
//    @AfterEach
//    fun tearDown() {
//        clearAllMocks()
//    }
//
//    private fun createSUT(): SerialGatewayConnector {
//        return SerialGatewayConnector(
//            gatewayFactory = gatewayFactory
//        )
//    }
//
//    // Success
//    @Test
//    fun `given the handshake succeeds, then the gateway is returned`() = runTest {
//        val sut = createSUT()
//
//        val actual = sut.connect(client)
//
//        assertEquals(gateway, actual)
//    }
//
//    @Test
//    fun `given the handshake succeeds, then the client stays connected`() = runTest {
//        val sut = createSUT()
//
//        sut.connect(client)
//
//        verify(exactly = 0) { client.disconnect() }
//    }
//
//    // Failure
//    @Test
//    fun `given the client fails to connect, then no gateway is returned`() = runTest {
//        val sut = createSUT()
//        every { client.connect() } throws nextException()
//
//        val actual = sut.connect(client)
//
//        assertNull(actual)
//    }
//
//    @Test
//    fun `given the handshake fails, then no gateway is returned`() = runTest {
//        val sut = createSUT()
//        coEvery { client.request(identificationCommand, any(), GatewayIdentificationResponse::class.java) } throws nextException()
//
//        val actual = sut.connect(client)
//
//        assertNull(actual)
//    }
//
//    @Test
//    fun `given the handshake fails, then client is disconnected`() = runTest {
//        val sut = createSUT()
//        coEvery { client.request(identificationCommand, any(), GatewayIdentificationResponse::class.java) } throws nextException()
//
//        sut.connect(client)
//
//        verify { client.disconnect() }
//    }
//
//}
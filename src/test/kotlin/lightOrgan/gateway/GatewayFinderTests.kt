//package lightOrgan.gateway
//
//import io.mockk.clearAllMocks
//import io.mockk.coEvery
//import io.mockk.every
//import io.mockk.mockk
//import kotlinx.coroutines.test.runTest
//import lightOrgan.gateway.serial.SerialGateway
//import lightOrgan.gateway.serial.SerialGatewayConnector
//import lightOrgan.gateway.serial.client.SerialClientFactory
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import serial.SerialPort
//import serial.SerialPortFinder
//import serial.rpc.DefaultSerialRpcClient
//
//class GatewayFinderTests {
//
//    private val serialPortFinder: SerialPortFinder = mockk()
//    private val serialClientFactory: SerialClientFactory = mockk()
//    private val serialGatewayConnector: SerialGatewayConnector = mockk()
//
//    private val port1: SerialPort = mockk()
//    private val port2: SerialPort = mockk()
//    private val gatewayPort: SerialPort = mockk()
//
//    private val client1: DefaultSerialRpcClient = mockk()
//    private val client2: DefaultSerialRpcClient = mockk()
//    private val gatewayClient: DefaultSerialRpcClient = mockk()
//
//    private val gateway: SerialGateway = mockk()
//
//    @BeforeEach
//    fun setupHappyPath() {
//        every { serialPortFinder.find() } returns listOf(port1, port2, gatewayPort)
//
//        coEvery { serialClientFactory.create(port1) } returns client1
//        coEvery { serialClientFactory.create(port2) } returns client2
//        coEvery { serialClientFactory.create(gatewayPort) } returns gatewayClient
//
//        coEvery { serialGatewayConnector.connect(client1) } returns null
//        coEvery { serialGatewayConnector.connect(client2) } returns null
//        coEvery { serialGatewayConnector.connect(gatewayClient) } returns gateway
//    }
//
//    @AfterEach
//    fun tearDown() {
//        clearAllMocks()
//    }
//
//    private fun createSUT(): GatewayFinder {
//        return GatewayFinder(
//            serialPortFinder = serialPortFinder,
//            serialClientFactory = serialClientFactory,
//            serialGatewayConnector = serialGatewayConnector
//        )
//    }
//
//    @Test
//    fun `given a gateway is available, then return the gateway`() = runTest {
//        val sut = createSUT()
//
//        val actual = sut.find()
//
//        assertEquals(gateway, actual)
//    }
//
//    @Test
//    fun `given no gateway is available, then return null`() = runTest {
//        val sut = createSUT()
//        every { serialPortFinder.find() } returns listOf(port1, port2)
//
//        val actual = sut.find()
//
//        assertEquals(null, actual)
//    }
//
//}
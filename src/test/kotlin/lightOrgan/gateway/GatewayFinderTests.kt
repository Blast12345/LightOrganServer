package lightOrgan.gateway

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import wrappers.serial.SerialPort
import wrappers.serial.SerialPortFinder

class GatewayFinderTests {

    private val serialPortFinder: SerialPortFinder = mockk()
    private val serialGatewayConnector: SerialGatewayConnector = mockk()

    private val otherPort1: SerialPort = mockk()
    private val otherPort2: SerialPort = mockk()
    private val gatewayPort: SerialPort = mockk()

    private val gateway: Gateway = mockk()

    @BeforeEach
    fun setupHappyPath() {
        every { serialPortFinder.find() } returns listOf(otherPort1, otherPort2, gatewayPort)

        coEvery { serialGatewayConnector.connect(otherPort1) } returns null
        coEvery { serialGatewayConnector.connect(otherPort2) } returns null
        coEvery { serialGatewayConnector.connect(gatewayPort) } returns gateway
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun createSUT(): GatewayFinder {
        return GatewayFinder(
            serialPortFinder = serialPortFinder,
            serialGatewayConnector = serialGatewayConnector
        )
    }

    // Find
    @Test
    fun `given a gateway is available, then return the gateway`() = runTest {
        val sut = createSUT()

        val actual = sut.find()

        assertEquals(gateway, actual)
    }

    @Test
    fun `given no gateway is available, then return null`() = runTest {
        val sut = createSUT()
        every { serialPortFinder.find() } returns listOf(otherPort1, otherPort2)

        val actual = sut.find()

        assertEquals(null, actual)
    }

}